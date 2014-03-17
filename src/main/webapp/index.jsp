<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>RDF auto builder</title>
        <link rel="stylesheet" type="text/css" href="resources/css/style.css" />
        <script src="http://code.jquery.com/jquery-1.9.1.js"></script>
        <script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
        <link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
        <script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?libraries=places&language=eng&sensor=false"></script>
    </head>
    <body>
        <h1>NaUKMA diploma work. Ihor Reshetnov</h1>
        <p>This work is about iterative terminology construction in RDF format</p>
        <h2>Jersey RESTful Web Application!</h2>
        <p><a href="webapi/myresource">Jersey resource</a>
        <p>Visit <a href="http://jersey.java.net">Project Jersey website</a>
            for more information on Jersey!

        <p>Upload Files Section</p>
        <form enctype="multipart/form-data" action="webapi/myresource/upload" method="post">
            <input id="pdf-file" type="file" name="file"/>
            <label for="pdf-file">Оберіть pdf файл для індексування</label>
            <input type="submit" value="Завантажити"/>
        </form>
    </body>
</html>