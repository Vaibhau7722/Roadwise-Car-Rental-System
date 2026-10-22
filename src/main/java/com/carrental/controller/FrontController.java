package com.carrental.controller;

import com.carrental.dao.hibernate.HibernateCarRepository;
import com.carrental.dao.jdbc.*;
import com.carrental.entity.Car;
import com.carrental.exception.*;
import com.carrental.filter.SecurityFilter;
import com.carrental.model.BookingReceipt;
import com.carrental.model.UserSession;
import com.carrental.service.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FrontController extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(FrontController.class);

    private AuthService auth;
    private CarService cars;
    private BookingService bookings;
    private DashboardService dashboards;
    private ReportService reports;
    private JdbcAuthRepository authRepo;

    @Override
    public void init() {
        authRepo = new JdbcAuthRepository();
        auth = new AuthService(authRepo);
        cars = new CarService(new JdbcCarRepository(), new HibernateCarRepository());
        bookings = new BookingService(new JdbcBookingRepository(), new RentalPricingService());
        dashboards = new DashboardService(new JdbcDashboardRepository());
        reports = new ReportService(new JdbcReportRepository());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        dispatch(req, resp, false);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        dispatch(req, resp, true);
    }

    private void dispatch(HttpServletRequest req, HttpServletResponse resp, boolean post)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String path = req.getRequestURI().substring(req.getContextPath().length());
        if (path.isEmpty()) {
            path = "/";
        }

        // Serve files from src/main/webapp/assets even when this servlet is mapped to /*.
        // Forwarding to a path here could route back into this servlet, so stream the
        // resource directly from the deployed web application instead.
        if (!post && path.startsWith("/assets/")) {
            serveStaticResource(path, resp);
            return;
        }

        try {
            if (!post) {
                switch (path) {
                    case "/", "/home" -> home(req, resp);
                    case "/cars" -> carList(req, resp);
                    case "/cars/details" -> carDetails(req, resp);
                    case "/login" -> forward(req, resp, "/WEB-INF/views/auth/login.jsp");
                    case "/register" -> forward(req, resp, "/WEB-INF/views/auth/register.jsp");
                    case "/customer/dashboard" -> customerDashboard(req, resp);
                    case "/customer/bookings" -> customerBookings(req, resp);
                    case "/customer/bookings/details" -> customerBooking(req, resp);
                    case "/customer/profile" -> profile(req, resp);
                    case "/customer/invoices/details" -> invoice(req, resp);
                    case "/admin/dashboard" -> adminDashboard(req, resp);
                    case "/admin/cars" -> adminCars(req, resp);
                    case "/admin/cars/create" -> forward(req, resp, "/WEB-INF/views/admin/car-form.jsp");
                    case "/admin/cars/edit" -> editCar(req, resp);
                    case "/admin/bookings" -> adminBookings(req, resp);
                    case "/admin/bookings/details" -> adminBooking(req, resp);
                    case "/admin/rentals" -> adminRentals(req, resp);
                    case "/admin/reports/revenue" -> revenue(req, resp);
                    case "/admin/reports/utilization" -> utilization(req, resp);
                    default -> resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
            } else {
                switch (path) {
                    case "/login" -> login(req, resp);
                    case "/register" -> register(req, resp);
                    case "/logout" -> logout(req, resp);
                    case "/customer/bookings/create" -> createBooking(req, resp);
                    case "/customer/bookings/cancel" -> cancelCustomerBooking(req, resp);
                    case "/customer/profile/update" -> updateProfile(req, resp);
                    case "/customer/password/change" -> changePassword(req, resp);
                    case "/admin/cars/create" -> saveCar(req, resp, null);
                    case "/admin/cars/update" -> saveCar(req, resp, req.getParameter("id"));
                    case "/admin/cars/deactivate" -> deactivateCar(req, resp);
                    case "/admin/bookings/cancel" -> cancelAdminBooking(req, resp);
                    case "/admin/rentals/pickup" -> pickup(req, resp);
                    case "/admin/rentals/return" -> returnRental(req, resp);
                    default -> resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
            }
        } catch (Exception ex) {
            handle(req, resp, path, ex);
        }
    }

    private void serveStaticResource(String path, HttpServletResponse resp) throws IOException {
        // Prevent attempts to access parent directories.
        if (path.contains("..")) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        try (InputStream input = getServletContext().getResourceAsStream(path)) {
            if (input == null) {
                log.debug("Static resource not found: {}", path);
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            String contentType = getServletContext().getMimeType(path);
            if (contentType == null && path.toLowerCase().endsWith(".svg")) {
                contentType = "image/svg+xml";
            }
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            resp.setContentType(contentType);
            resp.setHeader("Cache-Control", "public, max-age=3600");
            input.transferTo(resp.getOutputStream());
        }
    }

    private void home(HttpServletRequest q, HttpServletResponse p) throws Exception {
        q.setAttribute("cars", cars.search(null, null, null, true).stream().limit(3).toList());
        forward(q, p, "/WEB-INF/views/public/home.jsp");
    }

    private void carList(HttpServletRequest q, HttpServletResponse p) throws Exception {
        String qText = q.getParameter("q");
        String category = q.getParameter("category");
        String maxRaw = q.getParameter("maxPrice");
        BigDecimal max = null;

        if (maxRaw != null && !maxRaw.isBlank()) {
            try {
                max = new BigDecimal(maxRaw);
                if (max.signum() < 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                throw new ValidationException("Maximum price must be a non-negative amount.");
            }
        }

        q.setAttribute("cars", cars.search(qText, category, max, true));
        q.setAttribute("categories", cars.categories());
        q.setAttribute("q", qText);
        q.setAttribute("selectedCategory", category);
        q.setAttribute("maxPrice", maxRaw);
        forward(q, p, "/WEB-INF/views/public/cars.jsp");
    }

    private void carDetails(HttpServletRequest q, HttpServletResponse p) throws Exception {
        q.setAttribute("car", cars.find(longParam(q, "id")));
        forward(q, p, "/WEB-INF/views/public/car-details.jsp");
    }

    private void login(HttpServletRequest q, HttpServletResponse p) throws Exception {
        UserSession user = auth.login(q.getParameter("email"), q.getParameter("password"));
        if (user == null) {
            throw new ValidationException("Account was not found. Please register first.");
        }
        q.changeSessionId();
        q.getSession().setAttribute(SecurityFilter.USER, user);
        redirect(p, q, "ADMIN".equals(user.role()) ? "/admin/dashboard" : "/customer/dashboard");
    }

    private void register(HttpServletRequest q, HttpServletResponse p) throws Exception {
        auth.register(q.getParameter("fullName"), q.getParameter("email"),
                q.getParameter("phone"), q.getParameter("password"),
                q.getParameter("confirmPassword"));
        q.getSession().setAttribute("notice", "Account created. Sign in to start booking.");
        redirect(p, q, "/login");
    }

    private void logout(HttpServletRequest q, HttpServletResponse p) {
        HttpSession s = q.getSession(false);
        if (s != null) {
            s.invalidate();
        }
        redirect(p, q, "/");
    }

    private void customerDashboard(HttpServletRequest q, HttpServletResponse p) throws Exception {
        UserSession u = user(q);
        q.setAttribute("summary", dashboards.customer(u.customerId()));
        List<Map<String, Object>> all = bookings.forCustomer(u.customerId());
        q.setAttribute("bookings", all.stream().limit(5).toList());
        forward(q, p, "/WEB-INF/views/customer/dashboard.jsp");
    }

    private void customerBookings(HttpServletRequest q, HttpServletResponse p) throws Exception {
        q.setAttribute("bookings", bookings.forCustomer(user(q).customerId()));
        forward(q, p, "/WEB-INF/views/customer/bookings.jsp");
    }

    private void customerBooking(HttpServletRequest q, HttpServletResponse p) throws Exception {
        q.setAttribute("booking",
                bookings.customerBooking(user(q).customerId(), q.getParameter("id")));
        forward(q, p, "/WEB-INF/views/customer/booking-details.jsp");
    }

    private void createBooking(HttpServletRequest q, HttpServletResponse p) throws Exception {
        BookingReceipt r = bookings.create(user(q).customerId(), q.getParameter("carId"),
                q.getParameter("pickupDate"), q.getParameter("returnDate"));
        q.getSession().setAttribute("notice",
                "Booking " + r.reference() + " confirmed. Estimated total: $" + r.baseAmount() + ".");
        redirect(p, q, "/customer/bookings/details?id=" + r.id());
    }

    private void cancelCustomerBooking(HttpServletRequest q, HttpServletResponse p) throws Exception {
        if (!bookings.cancelCustomer(user(q).customerId(), q.getParameter("id"))) {
            throw new ValidationException("Only a future confirmed booking can be cancelled.");
        }
        q.getSession().setAttribute("notice", "Booking cancelled.");
        redirect(p, q, "/customer/bookings");
    }

    private void profile(HttpServletRequest q, HttpServletResponse p) throws Exception {
        q.setAttribute("profile", authRepo.profile(user(q).customerId()));
        forward(q, p, "/WEB-INF/views/customer/profile.jsp");
    }

    private void updateProfile(HttpServletRequest q, HttpServletResponse p) throws Exception {
        auth.updateProfile(user(q).customerId(), q.getParameter("fullName"), q.getParameter("phone"));
        q.getSession().setAttribute("notice", "Profile updated.");
        redirect(p, q, "/customer/profile");
    }

    private void changePassword(HttpServletRequest q, HttpServletResponse p) throws Exception {
        UserSession u = user(q);
        auth.changePassword(u.userId(), q.getParameter("currentPassword"),
                q.getParameter("newPassword"), q.getParameter("confirmPassword"));
        q.getSession().setAttribute("notice", "Password changed.");
        redirect(p, q, "/customer/profile");
    }

    private void invoice(HttpServletRequest q, HttpServletResponse p) throws Exception {
        q.setAttribute("booking", bookings.invoice(user(q).customerId(), q.getParameter("id")));
        forward(q, p, "/WEB-INF/views/customer/invoice.jsp");
    }

    private void adminDashboard(HttpServletRequest q, HttpServletResponse p) throws Exception {
        q.setAttribute("summary", dashboards.admin());
        q.setAttribute("bookings", bookings.all(null, null).stream().limit(8).toList());
        forward(q, p, "/WEB-INF/views/admin/dashboard.jsp");
    }

    private void adminCars(HttpServletRequest q, HttpServletResponse p) throws Exception {
        q.setAttribute("cars", cars.search(q.getParameter("q"), null, null, false));
        q.setAttribute("q", q.getParameter("q"));
        forward(q, p, "/WEB-INF/views/admin/cars.jsp");
    }

    private void editCar(HttpServletRequest q, HttpServletResponse p) throws Exception {
        q.setAttribute("car", cars.find(longParam(q, "id")));
        forward(q, p, "/WEB-INF/views/admin/car-form.jsp");
    }

    private void saveCar(HttpServletRequest q, HttpServletResponse p, String id) throws Exception {
        cars.save(id, q.getParameter("brand"), q.getParameter("model"),
                q.getParameter("registrationNumber"), q.getParameter("category"),
                q.getParameter("fuelType"), q.getParameter("transmission"),
                q.getParameter("seatingCapacity"), q.getParameter("dailyRate"),
                q.getParameter("status"), q.getParameter("imageUrl"));
        q.getSession().setAttribute("notice", id == null ? "Car added." : "Car updated.");
        redirect(p, q, "/admin/cars");
    }

    private void deactivateCar(HttpServletRequest q, HttpServletResponse p) throws Exception {
        cars.deactivate(longParam(q, "id"));
        q.getSession().setAttribute("notice",
                "Car deactivated. Its booking history has been retained.");
        redirect(p, q, "/admin/cars");
    }

    private void adminBookings(HttpServletRequest q, HttpServletResponse p) throws Exception {
        q.setAttribute("bookings", bookings.all(q.getParameter("q"), q.getParameter("status")));
        q.setAttribute("q", q.getParameter("q"));
        q.setAttribute("selectedStatus", q.getParameter("status"));
        forward(q, p, "/WEB-INF/views/admin/bookings.jsp");
    }

    private void adminBooking(HttpServletRequest q, HttpServletResponse p) throws Exception {
        q.setAttribute("booking", bookings.booking(q.getParameter("id")));
        forward(q, p, "/WEB-INF/views/admin/booking-details.jsp");
    }

    private void cancelAdminBooking(HttpServletRequest q, HttpServletResponse p) throws Exception {
        if (!bookings.cancelAdmin(q.getParameter("id"))) {
            throw new ValidationException("Only future confirmed bookings can be cancelled.");
        }
        q.getSession().setAttribute("notice", "Booking cancelled.");
        redirect(p, q, "/admin/bookings");
    }

    private void adminRentals(HttpServletRequest q, HttpServletResponse p) throws Exception {
        q.setAttribute("bookings", bookings.all(null, null));
        forward(q, p, "/WEB-INF/views/admin/rentals.jsp");
    }

    private void pickup(HttpServletRequest q, HttpServletResponse p) throws Exception {
        bookings.pickup(q.getParameter("id"));
        q.getSession().setAttribute("notice", "Pickup recorded; rental is now active.");
        redirect(p, q, "/admin/rentals");
    }

    private void returnRental(HttpServletRequest q, HttpServletResponse p) throws Exception {
        bookings.returnRental(q.getParameter("id"));
        q.getSession().setAttribute("notice",
                "Return recorded. Late fee and simulated payment were updated.");
        redirect(p, q, "/admin/rentals");
    }

    private void revenue(HttpServletRequest q, HttpServletResponse p) throws Exception {
        q.setAttribute("reports", reports.monthlyRevenue());
        q.setAttribute("reportTitle", "Monthly rental revenue");
        q.setAttribute("reportMode", "revenue");
        forward(q, p, "/WEB-INF/views/admin/reports.jsp");
    }

    private void utilization(HttpServletRequest q, HttpServletResponse p) throws Exception {
        q.setAttribute("reports", reports.mostRented());
        q.setAttribute("reportTitle", "Most frequently rented cars");
        q.setAttribute("reportMode", "cars");
        forward(q, p, "/WEB-INF/views/admin/reports.jsp");
    }

    private void handle(HttpServletRequest req, HttpServletResponse resp, String path, Exception ex)
            throws ServletException, IOException {
        if (ex instanceof ServletException se) {
            throw se;
        }
        if (ex instanceof IOException ioe) {
            throw ioe;
        }
        if (ex instanceof CarNotFoundException || ex instanceof BookingNotFoundException) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Record not found.");
            return;
        }
        if (ex instanceof ValidationException || ex instanceof InvalidBookingDatesException
                || ex instanceof CarNotAvailableException) {
            req.setAttribute("error", ex.getMessage());
            log.info("Rejected request {}: {}", path, ex.getMessage());
        } else {
            req.setAttribute("error",
                    "We could not complete your request. Check the database configuration and try again.");
            log.error("Request failed for {}", path, ex);
        }
        forward(req, resp, "/WEB-INF/views/errors/error.jsp");
    }

    private UserSession user(HttpServletRequest q) {
        return (UserSession) q.getSession(false).getAttribute(SecurityFilter.USER);
    }

    private long longParam(HttpServletRequest q, String name) {
        try {
            return Long.parseLong(q.getParameter(name));
        } catch (Exception ex) {
            throw new ValidationException("A valid record ID is required.");
        }
    }

    private void forward(HttpServletRequest q, HttpServletResponse p, String target)
            throws ServletException, IOException {
        q.getRequestDispatcher(target).forward(q, p);
    }

    private void redirect(HttpServletResponse p, HttpServletRequest q, String target) {
        p.setStatus(HttpServletResponse.SC_SEE_OTHER);
        p.setHeader("Location", q.getContextPath() + target);
    }
}
