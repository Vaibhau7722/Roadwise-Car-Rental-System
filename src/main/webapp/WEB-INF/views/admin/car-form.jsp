<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="editing" value="${not empty car}"/>
<c:set var="pageTitle" value="${editing ? 'Edit car' : 'Add car'} | Roadwise"/>

<c:choose>
    <c:when test="${editing}">
        <c:url var="formAction" value="/admin/cars/update"/>
    </c:when>
    <c:otherwise>
        <c:url var="formAction" value="/admin/cars/create"/>
    </c:otherwise>
</c:choose>

<%@ include file="../common/header.jsp" %>

<div class="page-heading">
    <span class="eyebrow">FLEET MANAGEMENT</span>

    <h1>
        <c:choose>
            <c:when test="${editing}">Edit car</c:when>
            <c:otherwise>Add a car</c:otherwise>
        </c:choose>
    </h1>

    <p>Enter vehicle specifications and current fleet status.</p>
</div>

<div class="form-card">
    <form method="post" action="${formAction}">

        <input type="hidden"
               name="_csrf"
               value="<c:out value='${requestScope.csrfToken}'/>">

        <c:if test="${editing}">
            <input type="hidden" name="id" value="${car.id}">
        </c:if>

        <div class="row g-3">

            <div class="col-md-6">
                <label class="form-label">Brand</label>
                <input class="form-control"
                       name="brand"
                       maxlength="60"
                       required
                       value="<c:out value='${car.brand}'/>"/>
            </div>

            <div class="col-md-6">
                <label class="form-label">Model</label>
                <input class="form-control"
                       name="model"
                       maxlength="80"
                       required
                       value="<c:out value='${car.model}'/>"/>
            </div>

            <div class="col-md-6">
                <label class="form-label">Registration number</label>
                <input class="form-control"
                       name="registrationNumber"
                       maxlength="25"
                       required
                       value="<c:out value='${car.registrationNumber}'/>"/>
            </div>

            <div class="col-md-6">
                <label class="form-label">Category</label>
                <input class="form-control"
                       name="category"
                       maxlength="40"
                       required
                       placeholder="Sedan, SUV, Hatchback"
                       value="<c:out value='${car.category}'/>"/>
            </div>

            <div class="col-md-4">
                <label class="form-label">Fuel type</label>
                <input class="form-control"
                       name="fuelType"
                       maxlength="30"
                       required
                       value="<c:out value='${car.fuelType}'/>"/>
            </div>

            <div class="col-md-4">
                <label class="form-label">Transmission</label>
                <input class="form-control"
                       name="transmission"
                       maxlength="30"
                       required
                       value="<c:out value='${car.transmission}'/>"/>
            </div>

            <div class="col-md-4">
                <label class="form-label">Seats</label>
                <input class="form-control"
                       type="number"
                       min="1"
                       max="20"
                       name="seatingCapacity"
                       required
                       value="<c:out value='${car.seatingCapacity}' default='5'/>"/>
            </div>

            <!-- Daily rental price in Indian Rupees -->
            <div class="col-md-6">
                <label class="form-label">Daily rental price (₹)</label>

                <div class="input-group">
                    <span class="input-group-text">₹</span>

                    <input class="form-control"
                           type="number"
                           min="0.01"
                           step="0.01"
                           name="dailyRate"
                           required
                           value="<c:out value='${car.dailyRate}'/>"/>
                </div>
            </div>

            <div class="col-md-6">
                <label class="form-label">Status</label>

                <select class="form-select" name="status">

                    <option value="AVAILABLE"
                        <c:if test="${empty car or car.status eq 'AVAILABLE'}">selected</c:if>>
                        Available
                    </option>

                    <option value="UNAVAILABLE"
                        <c:if test="${car.status eq 'UNAVAILABLE'}">selected</c:if>>
                        Unavailable
                    </option>

                    <option value="MAINTENANCE"
                        <c:if test="${car.status eq 'MAINTENANCE'}">selected</c:if>>
                        Under maintenance
                    </option>

                    <option value="INACTIVE"
                        <c:if test="${car.status eq 'INACTIVE'}">selected</c:if>>
                        Inactive
                    </option>

                </select>
            </div>

            <div class="col-12">
                <label class="form-label">Image URL or local path (optional)</label>

                <input class="form-control"
                       name="imageUrl"
                       maxlength="500"
                       value="<c:out value='${car.imageUrl}'/>"
                       placeholder="Leave blank to use the local fallback image">
            </div>

        </div>

        <div class="d-flex gap-2 mt-4">

            <button class="btn btn-primary">
                <c:choose>
                    <c:when test="${editing}">Save changes</c:when>
                    <c:otherwise>Add car</c:otherwise>
                </c:choose>
            </button>

            <a class="btn btn-outline-secondary"
               href="${pageContext.request.contextPath}/admin/cars">
                Cancel
            </a>

        </div>

    </form>
</div>

<%@ include file="../common/footer.jsp" %>