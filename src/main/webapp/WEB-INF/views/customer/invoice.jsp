<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="Rental invoice | Roadwise"/>

<%@ include file="../common/header.jsp" %>

<div class="d-flex justify-content-between align-items-start mb-3">
    <div>
        <span class="eyebrow">RENTAL INVOICE</span>
        <h1 class="h2">
            Invoice <c:out value="${booking.reference}"/>
        </h1>
    </div>

    <button class="btn btn-outline-primary no-print"
            onclick="window.print()">
        <i class="bi bi-printer me-1"></i>Print invoice
    </button>
</div>

<div class="invoice-card">

    <div class="row mb-4">
        <div class="col">
            <strong>Roadwise Car Rental</strong>
            <div class="text-secondary">Rental summary</div>
        </div>

        <div class="col text-end">
            <span class="status status-${booking.status}">
                <c:out value="${booking.status}"/>
            </span>
        </div>
    </div>

    <div class="row g-3 mb-4">

        <div class="col-md-6">
            <small>Booking reference</small>
            <div>
                <strong><c:out value="${booking.reference}"/></strong>
            </div>
        </div>

        <div class="col-md-6">
            <small>Customer</small>
            <div>
                <strong><c:out value="${booking.customerName}"/></strong>
            </div>
        </div>

        <div class="col-md-6">
            <small>Vehicle</small>
            <div>
                <c:out value="${booking.brand}"/>
                <c:out value="${booking.model}"/>
                (<c:out value="${booking.registrationNumber}"/>)
            </div>
        </div>

        <div class="col-md-6">
            <small>Rental dates</small>
            <div>
                <c:out value="${booking.pickupDate}"/>
                to
                <c:out value="${booking.returnDate}"/>
            </div>
        </div>

        <div class="col-md-6">
            <small>Rental days</small>
            <div>
                <c:out value="${booking.rentalDays}"/>
            </div>
        </div>

        <div class="col-md-6">
            <small>Agreed daily price</small>
            <div>
                ₹<c:out value="${booking.dailyRate}"/>
            </div>
        </div>

    </div>

    <table class="table">
        <tbody>

        <tr>
            <td>Base rental charge</td>
            <td class="text-end">
                ₹<c:out value="${booking.baseAmount}"/>
            </td>
        </tr>

        <tr>
            <td>
                Late fees (<c:out value="${booking.lateDays}"/> days)
            </td>
            <td class="text-end">
                ₹<c:out value="${booking.lateFee}"/>
            </td>
        </tr>

        <tr class="fs-5 fw-bold">
            <td>Final rental charge</td>
            <td class="text-end">
                ₹<c:out value="${booking.finalCharge}"/>
            </td>
        </tr>

        </tbody>
    </table>

    <p class="small text-secondary mb-0">
        Payment status is simulated for this demo system.
    </p>

</div>

<%@ include file="../common/footer.jsp" %>