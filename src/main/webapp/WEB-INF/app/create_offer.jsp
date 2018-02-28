<%-- 
    Document   : modify_category
    Created on : 28.02.2018, 18:09:42
    Author     : METELC
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib tagdir="/WEB-INF/tags/templates" prefix="template"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<template:base>
    <jsp:attribute name="title">
        Angebot anzeigen
    </jsp:attribute>

    <jsp:attribute name="head">
        <link rel="stylesheet" href="<c:url value="/css/task_edit.css"/>" />
    </jsp:attribute>

    <jsp:attribute name="menu">
        <div class="menuitem">
            <a href="<c:url value="/overview/"/>">Übersicht</a>
        </div>
        <div class="menuitem">
            <a class="icon-th"  href="<c:url value="/favorites/"/>">Favoriten</a>
        </div>
        <div class="menuitem">
            <a class="icon-cog" href="<c:url value="/app/kategorien/"/>">Kategorien bearbeiten</a>
        </div>
        <div class="menuitem">
            <a href="<c:url value="/logout/"/>">Einloggen</a>
        </div>
    </jsp:attribute>

    <jsp:attribute name="content">
       <%--  <form method="post" class="stacked"> --%>
       <form method="post" class="stacked">
            <div class="column">
                <%-- CSRF-Token --%>
                <input type="hidden" name="csrf_token" value="${csrf_token}">

                <%-- Eingabefelder --%>
                <label for="anzeige_kategorie">Kategorie:</label>
                <div class="side-by-side">
                    <select name="anzeige_kategorie">
                        <option value="">Keine Kategorie</option>

                        <c:forEach items="${kategorien}" var="kategorie">
                            <option value="${kategorie.slag}" ${show_offer_form.values["kategorien"][0] == kategorie.slag ? 'selected' : ''}>
                                  <c:out value="${kategorie.name}" />
                            </option>
                       
                        </c:forEach>
                    </select>
                </div>
                
                <label for="anzeige_art">Art des Angebots
                <span class="required">*</span>
                </label>
                <div class="side-by-side">
                    <select name="anzeige_art">
                        <option value="">Biete</option>

                        <c:forEach items="${arten}" var="art">
                            <option value="${art}" ${show_offer_form.values["arten"][0] == art ? 'selected' : ''}>
                                <c:out value="${art}" />
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <label for="anzeige_bezeichnung">
                    Bezeichnung:
                    <span class="required">*</span>
                </label>
                <div class="side-by-side">
                    <input readonly type="text" name="anzeige_bezeichnung" required value="${show_offer_form.values["bezeichnung"][0]}">
                </div>
                
                <label for="anzeige_beschreibung">
                    Beschreibung:
                </label>
                <div class="side-by-side">
                    <textarea name="anzeige_beschreibung"><c:out value="${show_offer_form.values['beschreibung'][0]}"/></textarea>
                </div>
                
                <label for="preis_art">
                    Preis:
                    <span class="preis_art">*</span>
                </label>
                <div class="side-by-side">
                     <select name="preis_art">
                        <c:forEach items="${preis_arten}" var="preis_art">
                            <option value="${preis_art}" ${show_offer_form.values["preis_art"][0] == status ? 'selected' : ''}>
                                <c:out value="${preis_art}"/>
                            </option>
                        </c:forEach>
                    </select>
                    <input readonly required type="text" name="preis" value="${show_offer_form.values["preis"][0]}">
                </div>
     
                <label>
                    Angelegt am:
                </label>
                 <div class="side-by-side">
                     <c:out value="${utils.formatDate(currentDate, datePattern)}"/>
                  </div>
                  
                  <label>
                    Anbieter
                    </label>
                 <div class="side-by-side">
                     <c:out value="${besitzername}"/>
                     <c:out value="${besitzer.getHausnummer()} ${besitzer.getStraße()}"/>
                     <c:out value="${besitzer.getPlz()} ${besitzer.getOrt()}"/>
                     <c:out value="${besitzer.getEmail()}"/>
                     <c:out value="${besitzer.getTelefonnummer()}"/>
                  </div>
                  

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
      <%--   </form> --%>
        </form>
    </jsp:attribute>
</template:base>