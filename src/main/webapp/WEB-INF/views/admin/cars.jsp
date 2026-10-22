<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="Manage cars | Roadwise"/>
<%@ include file="../common/header.jsp" %>

<div class="d-flex flex-wrap justify-content-between align-items-end gap-3 page-heading">
    <div>
        <span class="eyebrow">FLEET</span>
        <h1>Manage cars</h1>
        <p>Update specifications, prices, availability, and status.</p>
    </div>

    <a class="btn btn-primary"
       href="${pageContext.request.contextPath}/admin/cars/create">
        <i class="bi bi-plus-lg me-1"></i>Add car
    </a>
</div>

<form class="row g-2 mb-3"
      method="get"
      action="${pageContext.request.contextPath}/admin/cars">

    <div class="col-sm-6 col-md-4">
        <input class="form-control"
               name="q"
               value="<c:out value='${q}'/>"
               placeholder="Search all cars by brand or model">
    </div>

    <div class="col-auto">
        <button class="btn btn-outline-primary">Search</button>
    </div>
</form>

<div class="table-responsive">
    <table class="table data-table align-middle">

        <thead>
        <tr>
            <th>Vehicle</th>
            <th>Registration</th>
            <th>Category</th>
            <th>Daily rate (₹)</th>
            <th>Status</th>
            <th>Actions</th>
        </tr>
        </thead>

        <tbody>
        <c:forEach items="${cars}" var="car">
            <tr>
                <td>
                    <strong>
                        <c:out value="${car.brand}"/>
                        <c:out value="${car.model}"/>
                    </strong>

                    <div class="small text-secondary">
                        <c:out value="${car.fuelType}"/>
                        · <c:out value="${car.transmission}"/>
                        · <c:out value="${car.seatingCapacity}"/> seats
                    </div>
                </td>

                <td>
                    <c:out value="${car.registrationNumber}"/>
                </td>

                <td>
                    <c:out value="${car.category}"/>
                </td>

                <!-- Daily rate displayed in Indian Rupees -->
                <td>
                    ₹<c:out value="${car.dailyRate}"/>
                </td>

                <td>
                    <span class="status status-${car.status}">
                        <c:out value="${car.status}"/>
                    </span>
                </td>

                <td>
                    <div class="d-flex gap-2">

                        <a class="btn btn-sm btn-outline-primary"
                           href="${pageContext.request.contextPath}/admin/cars/edit?id=${car.id}">
                            Edit
                        </a>

                        <c:if test="${car.status ne 'INACTIVE'}">
                            <form method="post"
                                  action="${pageContext.request.contextPath}/admin/cars/deactivate"
                                  onsubmit="return confirm('Deactivate this car? Booking history will be retained.')">

                                <input type="hidden"
                                       name="_csrf"
                                       value="<c:out value='${requestScope.csrfToken}'/>">

                                <input type="hidden"
                                       name="id"
                                       value="${car.id}">

                                <button class="btn btn-sm btn-outline-danger">
                                    Deactivate
                                </button>
                            </form>
                        </c:if>

                    </div>
                </td>
            </tr>
        </c:forEach>

        <c:if test="${empty cars}">
            <tr>
                <td colspan="6" class="text-center text-secondary py-4">
                    No cars found.
                </td>
            </tr>
        </c:if>

        </tbody>
    </table>
</div>

<%@ include file="../common/footer.jsp" %>