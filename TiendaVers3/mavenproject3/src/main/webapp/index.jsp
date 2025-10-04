<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@ include file="vistas/head.jsp" %>

<head>
    <link rel="stylesheet" href="css/index.css"/>
    <link rel="stylesheet" href="css/header.css"/>
</head>

<%@include file="vistas/header.jsp" %> <!-- Incluye la cabecera -->

<main class="main">
    <div class="container-principal-uno">
        <div class="container-general-contenido-uno">
            <div class="container-imagen-uno">
                <img src="Imagenes/imagenuno.png" alt="">
            </div>
            <div class="container-contenido-uno">
                <div class="container-subtitulo-uno">
                    <h2>GRAN CAFÉ, GRAN COMIDA</h2>
                </div>
                <div class="container-descripcion-uno">
                    <p>
                        Relájese y disfrute de una taza de
                        café perfecta en nuestra cómoda 
                        cafetería.
                    </p>
                    <a href="ProductoControlle?accion=listar" id="btn-viewmenu">Ver Menú</a>
                </div>
            </div>   
        </div>    
    </div>

    <div class="container-principal-dos">
        <div class="container-general-contenido-dos">
            <div class="container-contenido-dos">
                <div class="container-subtitulo-dos">
                    <h2>DELICIOSOS PLATOS, BUENAS PREPARACIONES</h2>
                </div>
                <div class="container-descripcion-dos">
                    <p>
                        Ofrecemos una variedad de menús y
                        platos a la carta.
                    </p>
                    <a href="sabores.html" id="btn-viewmenu">Ver Menú</a>
                </div>
            </div>
            <div class="container-imagen-dos">
                <img src="Imagenes/imagendos.png" alt="">
            </div>
        </div>
    </div>
</main>

<%@include file="vistas/footer.jsp" %> <!-- Incluye el pie de página -->
