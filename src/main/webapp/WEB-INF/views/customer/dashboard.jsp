<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="My dashboard | Roadwise"/>

<%@ include file="../common/header.jsp" %>

<div class="page-heading">
    <span class="eyebrow">CUSTOMER AREA</span>

    <h1>
        Welcome,
        <c:out value="${sessionScope.authenticatedUser.fullName}"/>
    </h1>

    <p>Here’s a quick look at your rentals.</p>
    <p class="text-secondary small">
        All rental prices are displayed in Indian Rupees (₹).
    </p>
</div>

<div class="row g-3 mb-4">

    <div class="col-6 col-lg-3">
        <div class="stat-card">
            <span>Total bookings</span>
            <strong>
                <c:out value="${summary.totalBookings}"/>
            </strong>
        </div>
    </div>

    <div class="col-6 col-lg-3">
        <div class="stat-card">
            <span>Upcoming</span>
            <strong>
                <c:out value="${summary.upcoming}"/>
            </strong>
        </div>
    </div>

    <div class="col-6 col-lg-3">
        <div class="stat-card">
            <span>Active rentals</span>
            <strong>
                <c:out value="${summary.active}"/>
            </strong>
        </div>
    </div>

    <div class="col-6 col-lg-3">
        <div class="stat-card">
            <span>Completed</span>
            <strong>
                <c:out value="${summary.completed}"/>
            </strong>
        </div>
    </div>

</div>

<div class="d-flex justify-content-between mb-3">
    <h2 class="h4">Recent bookings</h2>

    <a href="${pageContext.request.contextPath}/customer/bookings">
        All bookings
    </a>
</div>

<%@ include file="booking-table.jsp" %>

<a class="btn btn-primary mt-3"
   href="${pageContext.request.contextPath}/cars">
    Book a car
</a>

<%@ include file="../common/footer.jsp" %>