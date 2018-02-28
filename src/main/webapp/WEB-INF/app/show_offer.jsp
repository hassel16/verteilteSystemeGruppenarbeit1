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
            <a class="icon-logout" href="<c:url value="/logout/"/>">Ausloggen</a>
        </div>
    </jsp:attribute>

    <jsp:attribute name="content">
       <%--  <form method="post" class="stacked"> --%>
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
                          <%--  <option value="${art}" ${show_offer_form.values["arten"][0] == art ? 'selected' : ''}>
                                <c:out value="${art}" />
                            </option>--%>
                          <c:out value="${art}" />   <%-- added for the above --%>
                        </c:forEach>
                    </select>
                </div>

                <label for="anzeige_bezeichnung">
                    Bezeichnung:
                    <span class="required">*</span>
                </label>
                <div class="side-by-side">
                    <input readonly type="text" name="anzeige_bezeichnung" value="${show_offer_form.values["bezeichnung"][0]}">
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
                            <%--
                            <option value="${preis_art}" ${show_offer_form.values["preis_art"][0] == status ? 'selected' : ''}>
                               
                                <c:out value="${preis_art}"/>
                            </option> --%>
                            <c:out value="${preis_art}"/> <%-- added for the above --%>
                        </c:forEach>
                    </select>
                    <input readonly type="text" name="preis" value="${show_offer_form.values["preis"][0]}">
                </div>
                
                <label>
                    Angelegt am:
                </label>
                 <div class="side-by-side">
                     <c:out value="${angelegt_am}"/>
                  </div>
                  
                  <label>
                    Anbieter
                    </label>
                 <div class="side-by-side">
                     <c:out value="${besitzername}"/>
                     <c:out value="${besitzer.nummer_straße}"/>
                     <c:out value="${besitzer.plz_city}"/>
                     <c:out value="${besitzer.email}"/>
                     <c:out value="${besitzer.tel}"/>
                  </div>
                  

                <%-- Button zum Abschicken --%>
               
            </div>

            <%-- Fehlermeldungen --%>
           
      <%--   </form> --%>
    </jsp:attribute>
</template:base>