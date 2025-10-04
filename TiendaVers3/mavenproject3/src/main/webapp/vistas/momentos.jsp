<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<%@ include file="head.jsp" %>
<%@ include file="header.jsp" %>
<link rel="stylesheet" href="<%= request.getContextPath()%>/css/momentos.css">

<!DOCTYPE html>
<html lang="es">
<head>
    <title>Momentos Tosta</title>
</head>

<body>
<main class="main-momentos">

    <!-- Título -->
    <h1 class="Titulo">🌟 Momentos Tosta 🌟</h1>

    <!-- Carrusel -->
    <section class="carrusel">
        <img class="flechas" id="atras" src="<%= request.getContextPath()%>/Imagenes/atras.png" alt="Flecha atrás">

        <div class="galeria-wrapper">
            <div class="galeria" id="galeria">
                <!-- JS insertará las imágenes aquí -->
            </div>
        </div>

        <img class="flechas" id="adelante" src="<%= request.getContextPath()%>/Imagenes/adelante.png" alt="Flecha adelante">
    </section>

    <div class="puntos" id="puntos"></div>

    <!-- Eventos -->
    <section class="eventos-section">
        <h2 class="evltra">🎉 Eventos Próximos</h2>
        <p class="evntdescrip">
            Descubre nuestras noches temáticas, talleres de barismo y shows en vivo.
        </p>

        <div class="tarjetas">
            <div class="eventos">
                <div class="imgE">
                    <img class="imgEvento" src="<%= request.getContextPath()%>/Imagenes/karaoke.jpg" alt="Karaoke">
                </div>
                <h3 class="nombreEvento">🎤 Lunes de Karaoke</h3>
                <p class="descripcionEvento">Ven a cantar con amigos y disfruta nuestras bebidas.</p>
                <p class="horarioEvento">7:30 pm - 10:00 pm</p>
            </div>

            <div class="eventos">
                <div class="imgE">
                    <img class="imgEvento" src="<%= request.getContextPath()%>/Imagenes/LOGO_CIRCULO_NEGRO.png" alt="Rock">
                </div>
                <h3 class="nombreEvento">🎸 Miércoles de Rock</h3>
                <p class="descripcionEvento">Una hora intensa con nuestra banda local.</p>
                <p class="horarioEvento">9:30 pm - 10:30 pm</p>
            </div>

            <div class="eventos">
                <div class="imgE">
                    <img class="imgEvento" src="<%= request.getContextPath()%>/Imagenes/varista.jpg" alt="Baristas">
                </div>
                <h3 class="nombreEvento">☕ Viernes de Baristas</h3>
                <p class="descripcionEvento">Aprende a preparar café con Oscar DelaFuente.</p>
                <p class="horarioEvento">7:30 pm - 9:00 pm</p>
            </div>
        </div>
    </section>

    <!-- Formulario -->
    <section class="formulario">
        <div class="englobe">
            <div class="txt_formulario">
                <h2>Ven y pásala bien con nosotros</h2>
                <p>¿Comentarios o sugerencias? Llena el formulario y cuéntanos.</p>
            </div>

            <form class="inputBox" action="https://formspree.io/f/mldnzknq" method="POST">
                <label class="textLabel" for="nombres">Nombre</label>
                <input class="caja" type="text" name="nombres" id="nombres" required>

                <label class="textLabel" for="apellido">Apellido</label>
                <input class="caja" type="text" name="apellido" id="apellido" required>

                <label class="textLabel" for="correo">Correo</label>
                <input class="caja" type="email" name="correo" id="correo" required>

                <label class="textLabel" for="telefono">Teléfono</label>
                <input class="caja" type="tel" name="telefono" id="telefono">

                <label class="textLabel" for="asunto">Mensaje</label>
                <textarea class="asunto" name="asunto" id="asunto" required></textarea>

                <input class="boton_formulario" type="submit" value="Enviar">
            </form>
        </div>
    </section>

</main>

<%@ include file="footer.jsp" %>

<!-- Script Carrusel -->
<script>
document.addEventListener("DOMContentLoaded", function () {
  const contextPath = '<%= request.getContextPath() %>';
  const rutas = [
    'img1_prim.PNG', 'im1_cutert.PNG', 'in3_secun.PNG',
    'in4_prim.PNG', 'ing2_prim.PNG', 'in1_terc.PNG',
    'im1_sext.PNG', 'img2_setc.PNG', 'img3_sext.PNG',
    'im1_quint.PNG', 'imagenuno.png'
  ].map(img => contextPath + "/Imagenes/" + img);

  const galeria = document.getElementById("galeria");

  rutas.forEach(src => {
    const div = document.createElement("div");
    div.className = "img-container";
    const img = document.createElement("img");
    img.src = src;
    img.className = "img";
    div.appendChild(img);
    galeria.appendChild(div);
  });

  let currentSlide = 0;
  const itemsPerView = 3;

  function updateSlider() {
    const itemWidth = galeria.querySelector(".img-container").offsetWidth + 24;
    const maxSlide = Math.ceil(rutas.length / itemsPerView) - 1;
    galeria.style.transform = `translateX(-${currentSlide * itemWidth * itemsPerView}px)`;
  }

  document.getElementById("adelante").addEventListener("click", () => {
    const maxSlide = Math.ceil(rutas.length / itemsPerView) - 1;
    if (currentSlide < maxSlide) currentSlide++;
    else currentSlide = 0;
    updateSlider();
  });

  document.getElementById("atras").addEventListener("click", () => {
    const maxSlide = Math.ceil(rutas.length / itemsPerView) - 1;
    if (currentSlide > 0) currentSlide--;
    else currentSlide = maxSlide;
    updateSlider();
  });

  window.addEventListener("resize", updateSlider);
  setTimeout(updateSlider, 500); // Asegura que se calcule tras carga
});
</script>

</body>
</html>
