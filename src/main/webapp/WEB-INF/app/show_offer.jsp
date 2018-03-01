<%-- 
    Document   : changeUserData
    Created on : 28.02.2018, 14:08:19
    Author     : METELC
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib tagdir="/WEB-INF/tags/templates" prefix="template"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
wa
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
                    <input readonly type="text" name="anzeige_kategorie" value="${anzeige.getKategorie().getName()}">
                </div>
                
                <label for="anzeige_kategorie">Art</label>
                <div class="side-by-side">
                    <input readonly type="text" name="anzeige_kategorie" value="${anzeige.getArt()}">
                </div>

                <label for="anzeige_bezeichnung">
                    Bezeichnung:
                </label>
                <div class="side-by-side">
                    <input readonly type="text" name="anzeige_bezeichnung" value="${anzeige.getTitel()}">
                </div>
                
                <label for="anzeige_beschreibung">
                    Beschreibung:
                </label>
                <div class="side-by-side">
                    <textarea readonly name="anzeige_beschreibung"><c:out value="${anzeige.getBeschreibung()}"/></textarea>
                </div>
                
                <label for="preis_art">
                    Preis:
                </label>
                <div class="side-by-side">
                   <input readonly type="text" name="preis_art" value="${anzeige.getPreisArt()}">
                    <input readonly type="text" name="preis" value="${anzeige.getPreis()}">
                </div>
                
                <label>
                    Angelegt am:
                </label>
                 <div class="side-by-side">
                     <c:out value="$${utils.formatDate(anzeige.getDatum(), datePattern)}"/>
                  </div>
                  
                  <label>
                    Anbieter
                    </label>
                 <div class="side-by-side">
                     <c:out value="${anzeige.getBesitzer().getVorname()} ${anzeige.getBesitzer().getNachname()}"/>
                     <c:out value="${anzeige.getBesitzer().getHausnummer()} ${anzeige.getBesitzer().getStraße()}"/>
                     <c:out value="${anzeige.getBesitzer().getPlz()} ${anzeige.getBesitzer().getOrt()}"/>
                     <c:out value="${anzeige.getBesitzer().getLand()}"/>
                     <c:out value="${anzeige.getBesitzer().getEmail()}"/>
                     <c:out value="${anzeige.getBesitzer().getTelefonnummer()}"/>
                  </div>
                  

                <%-- Button zum Abschicken --%>
               
            </div>

            <%-- Fehlermeldungen --%>
           
      <%--   </form> --%>
    </jsp:attribute>
</template:base>