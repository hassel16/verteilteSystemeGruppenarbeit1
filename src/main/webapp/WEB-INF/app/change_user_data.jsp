<%-- 
    Document   : changeUserData
    Created on : 28.02.2018, 14:08:19
    Author     : METELC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib tagdir="/WEB-INF/tags/templates" prefix="template"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:set var="base_url" value="<%=request.getContextPath()%>" />

<template:base>
    <jsp:attribute name="title">
        Benutzerprofil bearbeiten
    </jsp:attribute>

    <jsp:attribute name="head">
        <link rel="stylesheet" href="<c:url value="/css/login.css"/>" />
    </jsp:attribute>
        
    <jsp:attribute name="menu">
        <div class="menuitem">
            <a href="<c:url value="/overview/"/>">Übersicht</a>
        </div>
        <div class="menuitem">
            <a class="icon-th"  href="<c:url value="/favorites/"/>">Favoriten</a>
        </div>
        <div class="menuitem">
            <a class="icon-logout" href="<c:url value="/logout/"/>">Ausloggen</a>
        </div>
    </jsp:attribute>

    <jsp:attribute name="content">
        <div class="container">
            <form method="post" class="stacked">
                <div class="column">
                    <%-- CSRF-Token --%>
                    <input type="hidden" name="csrf_token" value="${csrf_token}">
                    <h2 >
                        Passwort ändern
                        </h2>
                    <%-- Eingabefelder --%>
                    <label for="change_username">
                        Benutzername:
                    </label>
                    <input type="text" name="change_username" value="${change_form.values["change_username"][0]}">

                    <label for="change_old_password">
                        Altes Passwort:
                        <span class="required">*</span>
                    </label>
                    <input type="password" name="change_old_password" value="${change_form.values["change_old_password"][0]}">

                    <label for="change_new_password1">
                        Neues Passwort:
                        <span class="required">*</span>
                    </label>
                    <input type="password" name="change_new_password1" value="${change_form.values["change_new_password1"][0]}">
                     
                    <label for="change_new_password2">
                        Neues Passwort (wdh.):
                        <span class="required">*</span>
                    </label>
                    <input type="password" name="change_new_password2" value="${change_form.values["change_new_password2"][0]}">

                    <h2 >
                        Anschrift
                        </h2>
                    <%-- Eingabefelder --%>
                    <label for="change_name">
                        Vor- und Nachname
                        <span class="required">*</span>
                    </label>
                    <input type="text" name="change_name" value="${change_form.values["change_name"][0]}">

                    <label for="change_street_number">
                        Straße und Hausnummer
                        <span class="required">*</span>
                    </label>
                    <input type="text" name="change_street_number" value="${change_form.values["change_street_number"][0]}">

                    <label for="change_plz_and_city">
                        Postleitzahl und Ort
                        <span class="required">*</span>
                    </label>
                    <input type="text" name="change_plz_and_city" value="${change_form.values["change_plz_and_city"][0]}">
                    <h2 >
                        Kontaktdaten
                        </h2>
                    <%-- Eingabefelder --%>
                    <label for="change_email">
                        E-Mail
                        <span class="required">*</span>
                    </label>
                    <input type="text" name="change_email" value="${change_form.values["change_email"][0]}">

                    <label for="change_tel">
                        Telefonnummer
                        </label>
                    <input type="text" name="change_tel" value="${change_form.values["change_tel"][0]}">
                    <%-- Button zum Abschicken --%>
                    <button class="icon-pencil" type="submit">
                        Daten ändern
                    </button>
                </div> 

                <%-- Fehlermeldungen --%>
                <c:if test="${!empty change_form.errors}">
                    <ul class="errors">
                        <c:forEach items="${change_form.errors}" var="error">
                            <li>${error}</li>
                            </c:forEach>
                    </ul>
                </c:if>
            </form>
        </div>
    </jsp:attribute>
</template:base>
