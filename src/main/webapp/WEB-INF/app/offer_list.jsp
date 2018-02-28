<%-- 
    Document   : anzeige_list
    Created on : 28.02.2018, 17:58:04
    Author     : METELC
--%>
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib tagdir="/WEB-INF/tags/templates" prefix="template"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<template:base>
    <jsp:attribute name="title">
        Übersicht
    </jsp:attribute>

    <jsp:attribute name="head">
        <link rel="stylesheet" href="<c:url value="/css/task_list.css"/>" />
    </jsp:attribute>

    <jsp:attribute name="menu">
        <div class="menuitem">
            <a href="<c:url value="/app/task/new/"/>">Angebot anlegen</a>
        </div>

        <div class="menuitem">
            <a href="<c:url value="/app/kategorien/"/>">Kategorien bearbeiten</a>
        </div>
        <div class="menuitem">
            <a href="<c:url value="/app/benutzer/"/>">Benutzer bearbeiten</a>
        </div>
        
        <div class="menuitem">
            <a class="icon-logout" href="<c:url value="/logout/"/>">Ausloggen</a>
        </div>
    </jsp:attribute>

    <jsp:attribute name="content">
        <%-- Suchfilter --%>
        <form method="GET" class="horizontal" id="search">
            <input type="text" name="search_text" value="${param.search_text}" placeholder="Beschreibung"/>

            <select name="search_kategorie">
                <option value="">Alle Kategorien</option>

                <c:forEach items="${kategorien}" var="category">
                    <option value="${kategorie.slag}" ${param.search_kategorie == kategorie.slug ? 'selected' : ''}>
                        <c:out value="${kategorie.name}" />
                    </option>
                </c:forEach>
            </select>

            <select name="search_angebotsart">
                <option value="">Alle Angebotsarten</option>

                <c:forEach items="${angebotsarten}" var="art">
                    <option value="${art}" ${param.search_art == art ? 'selected' : ''}>
                        <c:out value="${art}"/>
                    </option>
                </c:forEach>
            </select>

            <button class="icon-search" type="submit">
                Suchen
            </button>
        </form>

        <%-- Gefundene Aufgaben --%>
        <c:choose>
            <c:when test="${empty anzeigen}">
                <p>
                    Es wurden keine Anzeigen gefunden. 🐈
                </p>
            </c:when>
            <c:otherwise>
                <jsp:useBean id="utils" class="dhbwka.wwi.vertsys.javaee.jtodo.web.WebUtils"/>
                
                <table>
                    <thead>
                        <tr>
                            <th>Bezeichnung</th>
                            <th>Kategorie</th>
                            <th>Besitzer</th>
                            <th>Angebotstyp</th>
                            <th>Preis</th>
                            <th>Preistyp</th>
                            <th>Datum</th>
                        </tr>
                    </thead>
                    <c:forEach items="${anzeigen}" var="anzeige">
                        <tr>
                            <td>
                                <a href="<c:url value="/app/task/${anzeige.id}/"/>">
                                    <c:out value="${anzeige.titel}"/>
                                </a>
                            </td>
                            <td>
                                <c:out value="${anzeige.kategorie.name}"/>
                            </td>
                            <td>
                                <c:out value="${anzeige.besitzer.benutzername}"/>
                            </td>
                            <td>
                                <c:out value="${anzeige.art}"/>
                            </td>
                            <td>
                                <c:out value="${anzeige.preis}"/>
                            </td>
                            <td>
                                <c:out value="${anzeige.preistyp}"/>
                            </td>
                            <td>
                                <c:out value="${utils.formatDate(anzeige.datum)}"/>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </c:otherwise>
        </c:choose>
    </jsp:attribute>
</template:base>