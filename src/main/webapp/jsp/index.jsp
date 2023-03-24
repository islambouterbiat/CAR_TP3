<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>


	<head>
	    <title>Map Reducer utilisation</title>
	</head>
 <body>

<h1>TP3 - Akka</h1>

		<h2> Initialisation des acteurs Akka</h2>
		<form action="init" method="post">
		    <input type="submit" value="Init"/>
		</form>

	<hr/>

		<h2> Fichier à analyser </h2>
		<% String fichier =(String) request.getAttribute("fichier");
		   
		   if (fichier!=null) {
		       %>
		       
		<p> L'analyse du fichier ${fichier} est faite </p>
		<%}%>


		<form action="analyser" method="post">
		    <label for="fichier">Le fichier à analyser :</label>
		    <input type="text" name="fichier" id="fichier"/>
		    <input type="submit" value="Analyser"/>
		</form>

	<hr/>

		<h2> Résultat </h2>
		<% String mot =(String) request.getAttribute("mot");
		   
		   if (mot!=null) {
		       %>
		       
		<p> le mot ${mot} apparait ${nombre} fois </p>
		<%}%>
		<form action="resultat" method="post">
		    <label for="mot">Mot à rechercher :</label>
		    <input type="text" name="mot" id="mot"/>
		    <input type="submit" value="Résultat"/>
		</form>

 </body>
</html>
