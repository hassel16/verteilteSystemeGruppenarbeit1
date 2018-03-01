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
        Favoritenübersicht
    </jsp:attribute>

    <jsp:attribute name="head">
        <link rel="stylesheet" href="<c:url value="/css/task_list.css"/>" />
    </jsp:attribute>

    <jsp:attribute name="menu">
        <div class="menuitem">
            <a href="<c:url value="/app/tasks/"/>">Übersicht</a>
        </div>
        <div class="menuitem">
            <a href="<c:url value="/app/task/new/"/>">Angebot anlegen</a>
        </div>

        <div class="menuitem">
            <a href="<c:url value="/app/categories/"/>">Kategorien bearbeiten</a>
        </div>

        <div class="menuitem">
            <a href="<c:url value="/app/edit_user/"/>">Benutzer bearbeiten</a>
        </div>
    </jsp:attribute>

    <jsp:attribute name="content">
        <%-- Suchfilter --%>

        <%-- Gefundene Aufgaben --%>
        <c:choose>
            <c:when test="${empty tasks}">
                <p>
                    Es wurden keine Aufgaben gefunden. 🐈
                </p>
            </c:when>
            <c:otherwise>
                <jsp:useBean id="utils" class="web.WebUtils"/>
                
                <table>
                    <thead>
                        <tr>
                            <th>Bezeichnung</th>
                            <th>Kategorie</th>
                            <th>Benutzer</th>
                            <th>Angebotstyp</th>
                            <th>Preis</th>
                            <th>Preistyp</th>
                            <th>Datum</th>
                            <th>Abwählen</th>
                        </tr>
                    </thead>
                    <c:forEach items="${tasks}" var="task">
                        <tr>
                            <form method="post" class="stacked">
                            <td>
                                <a href="<c:url value="/app/task/${task.id}/"/>">
                                    <c:out value="${task.titel}"/>
                                </a>
                                <input type="hidden" name="csrf_token" value="${csrf_token}">
                                <input type="hidden" name="task_id_defavorisieren" value="${task.id}">
                            </td>
                            <td>
                                <c:out value="${task.kategorie.name}"/>
                            </td>
                            <td>
                                <c:out value="${task.benutzer.benutzername}"/>
                            </td>
                            <td>
                                <c:out value="${task.art}"/>
                            </td>
                            <td>
                                <c:out value="${task.preisvorstellung}"/>
                            </td>
                            <td>
                                <c:out value="${task.artDesPreises}"/>
                            </td>
                            <td>
                                <c:out value="${task.formatEinstellungsdatum()}"/>
                            </td>
                            <td>
                                <button class="icon-th" type="submit">
                                    
                                </button>
                            </td>
                            </form>
                        </tr>
                    </c:forEach>
                </table>
            </c:otherwise>
        </c:choose>
    </jsp:attribute>
</template:base>