<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<c:set var="pageTitle" value="Browse cars | Roadwise"/>
<%@ include file="../common/header.jsp" %>

<div class="page-heading">
    <span class="eyebrow">THE FLEET</span>
    <h1>Find your car</h1>
    <p>Search the available fleet and compare daily rates in Indian Rupees (₹).</p>
</div>

<form class="filter-panel row g-2 mb-4"
      method="get"
      action="${pageContext.request.contextPath}/cars">

    <div class="col-md-5">
        <label class="form-label" for="q">Brand or model</label>
        <input class="form-control"
               id="q"
               name="q"
               value="<c:out value='${q}'/>"
               placeholder="e.g. Toyota">
    </div>

    <div class="col-md-3">
        <label class="form-label" for="category">Category</label>
        <select class="form-select" id="category" name="category">
            <option value="">All categories</option>

            <c:forEach items="${categories}" var="cat">
                <option value="<c:out value='${cat}'/>"
                    <c:if test="${selectedCategory eq cat}">selected</c:if>>
                    <c:out value="${cat}"/>
                </option>
            </c:forEach>
        </select>
    </div>

    <div class="col-md-2">
        <label class="form-label" for="maxPrice">Max daily price (₹)</label>
        <input class="form-control"
               type="number"
               min="0"
               step="0.01"
               id="maxPrice"
               name="maxPrice"
               value="<c:out value='${maxPrice}'/>"
               placeholder="e.g. 3000">
    </div>

    <div class="col-md-2 d-flex align-items-end">
        <button class="btn btn-primary w-100">
            <i class="bi bi-search me-1"></i>Search
        </button>
    </div>
</form>

<div class="row g-3">

    <c:forEach items="${cars}" var="car">

        <div class="col-md-6 col-lg-4">
            <div class="card car-card h-100">

                <%-- Resolve the car image path. --%>
                <c:set var="carImage" value="${car.imageUrl}"/>

                <c:choose>
                    <c:when test="${empty carImage}">
                        <c:set var="carImage" value="/assets/images/car-placeholder.svg"/>
                    </c:when>

                    <c:when test="${fn:startsWith(carImage, 'http://') or fn:startsWith(carImage, 'https://')}">
                        <%-- Keep external image URL as-is. --%>
                    </c:when>

                    <c:when test="${fn:startsWith(carImage, '/')}">
                        <%-- Root-relative application path. --%>
                    </c:when>

                    <c:otherwise>
                        <%-- Filename only, such as innova.jpg. --%>
                        <c:set var="carImage" value="/assets/images/${carImage}"/>
                    </c:otherwise>
                </c:choose>

                <c:choose>
                    <c:when test="${fn:startsWith(carImage, 'http://') or fn:startsWith(carImage, 'https://')}">

                        <img src="<c:out value='${carImage}'/>"
                             class="card-img-top car-image"
                             alt="<c:out value='${car.brand} ${car.model}'/>"
                             onerror="this.onerror=null;this.src='${pageContext.request.contextPath}/assets/images/car-placeholder.svg';">

                    </c:when>

                    <c:otherwise>

                        <img src="${pageContext.request.contextPath}<c:out value='${carImage}'/>"
                             class="card-img-top car-image"
                             alt="<c:out value='${car.brand} ${car.model}'/>"
                             onerror="this.onerror=null;this.src='${pageContext.request.contextPath}/assets/images/car-placeholder.svg';">

                    </c:otherwise>
                </c:choose>

                <div class="card-body">

                    <span class="badge text-bg-light mb-2">
                        <c:out value="${car.category}"/>
                    </span>

                    <h2 class="h5">
                        <c:out value="${car.brand}"/>
                        <c:out value="${car.model}"/>
                    </h2>

                    <p class="text-secondary mb-3">
                        <c:out value="${car.fuelType}"/> ·
                        <c:out value="${car.transmission}"/> ·
                        <c:out value="${car.seatingCapacity}"/> seats
                    </p>

                    <div class="d-flex justify-content-between align-items-center">

                        <!-- Daily rental price in Indian Rupees -->
                        <strong>
                            ₹<c:out value="${car.dailyRate}"/>
                            <small class="text-secondary fw-normal"> / day</small>
                        </strong>

                        <a class="btn btn-sm btn-primary"
                           href="${pageContext.request.contextPath}/cars/details?id=${car.id}">
                            View car
                        </a>

                    </div>
                </div>
            </div>
        </div>

    </c:forEach>

    <c:if test="${empty cars}">
        <div class="col-12">
            <div class="empty-state">
                <i class="bi bi-car-front"></i>
                <h2 class="h5">No matching cars</h2>
                <p>Try changing your search or price filter.</p>
            </div>
        </div>
    </c:if>

</div>

<%@ include file="../common/footer.jsp" %>