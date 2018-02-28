<%-- 
    Copyright © 2018 Dennis Schulmeister-Zimolong

    E-Mail: dhbw@windows3.de
    Webseite: https://www.wpvs.de/

    Dieser Quellcode ist lizenziert unter einer
    Creative Commons Namensnennung 4.0 International Lizenz.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib tagdir="/WEB-INF/tags/templates" prefix="template"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:set var="base_url" value="<%=request.getContextPath()%>" />

<template:base>
    <jsp:attribute name="title">
        Registrierung
    </jsp:attribute>

    <jsp:attribute name="head">
        <link rel="stylesheet" href="<c:url value="/css/login.css"/>" />
    </jsp:attribute>
        
    <jsp:attribute name="menu">
        <div class="menuitem">
            <a class="icon-cog" href="<c:url value="/favorites/"/>">Favoriten</a>
        </div>
        
        <div class="menuitem">
            <a class="icon-login" href="<c:url value="/logout/"/>">Einloggen</a>
        </div>
    </jsp:attribute>

    <jsp:attribute name="content">
        <div class="container">
            <form method="post" class="stacked">
                <div class="column">
                    <%-- CSRF-Token --%>
                    <input type="hidden" required name="csrf_token" value="${csrf_token}">
                    <h2 >
                        Logindaten
                        </h2>
                    <%-- Eingabefelder --%>
                    <label for="signup_username">
                        Benutzername:
                        <span class="required">*</span>
                    </label>
                    <input type="text" required name="signup_username" value="${signup_form.values["signup_username"][0]}">

                    <label for="signup_password1">
                        Passwort:
                        <span class="required">*</span>
                    </label>
                    <input type="password" required name="signup_password1" value="${signup_form.values["signup_password1"][0]}">

                    <label for="signup_password2">
                        Passwort (wdh.):
                        <span class="required">*</span>
                    </label>
                    <input type="password" required name="signup_password2" value="${signup_form.values["signup_password2"][0]}">

                    <h2 >
                        Anschrift
                        </h2>
                    <%-- Eingabefelder --%>
                    <label for="signup_vorname">
                        Vorname
                        <span class="required">*</span>
                    </label>
                    <input type="text" required name="signup_nachname" value="${signup_form.values["signup_vorname"][0]}">
                    
                    <label for="signup_nachname">
                        Nachname
                        <span class="required">*</span>
                    </label>
                    <input type="text" required name="signup_nachname" value="${signup_form.values["signup_nachname"][0]}">
 
                    <label for="signup_street">
                        Hausnummer und Straße
                        <span class="required">*</span>
                    </label>
                    <div class="side-by-side">
                    <input type="text" required name="signup_street_number" value="${signup_form.values["signup_street_number"][0]}">
                    <input type="text" required name="signup_street" value="${signup_form.values["signup_street"][0]}">
                    </div>
                    
                    <label for="signup_plz_city">
                        Postleitzahl und Ort
                        <span class="required">*</span>
                    </label>
                    <div class="side-by-side">
                    <input type="text" required name="signup_plz" value="${signup_form.values["signup_plz"][0]}">
                    <input type="text" required name="signup_city" value="${signup_form.values["signup_city"][0]}">
                    </div>
                    
                    <label for="signup_land">
                        Land
                        <span class="required">*</span>
                    </label>
                     <input type="text" required name="signup_land" value="${signup_form.values["signup_land"][0]}">
                    <h2 >
                        Kontaktdaten
                        </h2>
                    <%-- Eingabefelder --%>
                    <label for="signup_email">
                        E-Mail
                        <span class="required">*</span>
                    </label>
                    <input type="text" required name="signup_email" value="${signup_form.values["signup_email"][0]}">

                    <label for="signup_tel">
                        Telefonnummer
                        </label>
                    <input type="text" name="signup_tel" value="${signup_form.values["signup_tel"][0]}">
                    <%-- Button zum Abschicken --%>
                    <button class="icon-pencil" type="submit">
                        Registrieren
                    </button>
                </div> 

                <%-- Fehlermeldungen --%>
                <c:if test="${!empty signup_form.errors}">
                    <ul class="errors">
                        <c:forEach items="${signup_form.errors}" var="error">
                            <li>${error}</li>
                            </c:forEach>
                    </ul>
                </c:if>
            </form>
        </div>
    </jsp:attribute>
</template:base>