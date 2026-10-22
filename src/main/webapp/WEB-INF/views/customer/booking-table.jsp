<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="table-responsive">
    <table class="table data-table align-middle">

        <thead>
        <tr>
            <th>Reference</th>
            <th>Car</th>
            <th>Dates</th>
            <th>Days</th>
            <th>Total price (₹)</th>
            <th>Status</th>
            <th></th>
        </tr>
        </thead>

        <tbody>
        <c:forEach items="${bookings}" var="b">
            <tr>

                <td>
                    <strong>
                        <c:out value="${b.reference}"/>
                    </strong>
                </td>

                <td>
                    <c:out value="${b.brand}"/>
                    <c:out value="${b.model}"/>
                </td>

                <td>
                    <c:out value="${b.pickupDate}"/>
                    –
                    <c:out value="${b.returnDate}"/>
                </td>

                <td>
                    <c:out value="${b.rentalDays}"/>
                </td>

                <td>
                    ₹<c:out value="${b.totalAmount}"/>
                </td>

                <td>
                    <span class="status status-${b.status}">
                        <c:out value="${b.status}"/>
                    </span>
                </td>

                <td>
                    <a class="btn btn-sm btn-outline-primary"
                       href="${pageContext.request.contextPath}/customer/bookings/details?id=${b.id}">
                        Details
                    </a>
                </td>

            </tr>
        </c:forEach>

        <c:if test="${empty bookings}">
            <tr>
                <td colspan="7" class="text-center text-secondary py-4">
                    You don’t have any bookings yet.
                </td>
            </tr>
        </c:if>

        </tbody>
    </table>
</div>