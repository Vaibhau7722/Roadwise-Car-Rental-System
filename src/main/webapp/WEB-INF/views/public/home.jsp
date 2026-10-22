<%@ page contentType="text/html; charset=UTF-8" %><%@ taglib prefix="c" uri="jakarta.tags.core" %><%@ taglib prefix="fn" uri="jakarta.tags.functions" %><c:set var="pageTitle" value="Home | Roadwise"/><%@ include file="../common/header.jsp" %>

<section class="hero rounded-4 p-4 p-lg-5 mb-5">
    <div class="row align-items-center">
        <div class="col-lg-7">
            <span class="eyebrow">YOUR NEXT TRIP STARTS HERE</span>
            <h1 class="display-4 fw-bold mt-3">A better way to get there.</h1>
            <p class="lead text-secondary">Find a car that fits your plans, book online, and pick it up when you’re ready.</p>
            <div class="d-flex gap-2 flex-wrap">
                <a class="btn btn-primary btn-lg" href="${pageContext.request.contextPath}/cars">Browse available cars</a>
                <a class="btn btn-outline-secondary btn-lg" href="${pageContext.request.contextPath}/register">Create an account</a>
            </div>
        </div>
        <div class="col-lg-5 mt-4 mt-lg-0">
            <div class="hero-card">
                <i class="bi bi-car-front-fill"></i>
                <div>
                    <strong>Flexible rentals</strong>
                    <span>Clear daily rates. No payment gateway required for this demo.</span>
                </div>
            </div>
        </div>
    </div>
</section>

<div class="d-flex justify-content-between align-items-end mb-3">
    <div>
        <span class="eyebrow">READY WHEN YOU ARE</span>
        <h2 class="h3 mb-0">Featured cars</h2>
    </div>
    <a href="${pageContext.request.contextPath}/cars" class="link-primary">
        See all cars <i class="bi bi-arrow-right"></i>
    </a>
</div>

<div class="row g-3">
    <c:forEach items="${cars}" var="car">
        <div class="col-md-6 col-lg-4">
            <div class="card car-card h-100">

                <c:choose>
                    <c:when test="${not empty car.imageUrl}">
                        <c:choose>
                            <c:when test="${fn:startsWith(car.imageUrl, '/assets/images/')}">
                                <img src="${pageContext.request.contextPath}<c:out value='${car.imageUrl}'/>"
                                     class="card-img-top car-image"
                                     alt="<c:out value='${car.brand} ${car.model}'/>">
                            </c:when>
                            <c:otherwise>
                                <img src="<c:out value='${car.imageUrl}'/>"
                                     class="card-img-top car-image"
                                     alt="<c:out value='${car.brand} ${car.model}'/>">
                            </c:otherwise>
                        </c:choose>
                    </c:when>
                    <c:otherwise>
                        <img src="${pageContext.request.contextPath}/assets/images/car-placeholder.svg"
                             class="card-img-top car-image"
                             alt="Car">
                    </c:otherwise>
                </c:choose>

                <div class="card-body">
                    <span class="badge text-bg-light mb-2">
                        <c:out value="${car.category}"/>
                    </span>

                    <h3 class="h5">
                        <c:out value="${car.brand}"/> <c:out value="${car.model}"/>
                    </h3>

                    <p class="text-secondary">
                        <c:out value="${car.transmission}"/> ·
                        <c:out value="${car.seatingCapacity}"/> seats ·
                        <c:out value="${car.fuelType}"/>
                    </p>

                    <div class="d-flex justify-content-between align-items-center">
                        <strong>
                            ₹<c:out value="${car.dailyRate}"/>
                            <small class="text-secondary fw-normal"> / day</small>
                        </strong>

                        <a class="btn btn-sm btn-outline-primary"
                           href="${pageContext.request.contextPath}/cars/details?id=${car.id}">
                            Details
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </c:forEach>

    <c:if test="${empty cars}">
        <div class="col">
            <p class="text-secondary">No cars are available right now.</p>
        </div>
    </c:if>
</div>

<%@ include file="../common/footer.jsp" %>