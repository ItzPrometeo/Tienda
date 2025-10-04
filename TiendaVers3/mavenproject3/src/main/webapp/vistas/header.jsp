<%@ page session="true" %>
<%@ page import="model.Cliente" %>

<%
    String currentPage = request.getRequestURI();
    boolean mostrarBuscador = currentPage.contains("listar.jsp");
    boolean mostrarBotonPedido = !currentPage.contains("listar.jsp");

    Cliente clienteHeader = null;
    String nombreCorto = "";

    Object cliObj = session.getAttribute("cliente");
    if (cliObj instanceof Cliente) {
        clienteHeader = (Cliente) cliObj;
        nombreCorto = clienteHeader.getNombreCompleto().split(" ")[0];
    }
%>

<header class="header">
    <!-- LOGO -->
    <div class="container-logo">
        <a href="<%= request.getContextPath()%>/index.jsp">
            <img src="<%= request.getContextPath()%>/Imagenes/logotosta1.png" alt="logotosta">
        </a>
    </div>

    <!-- MENÚ Y BUSCADOR -->
    <div class="container-nav">
        <nav class="nav">
            <div class="nav-left">
                <ul class="nav-list">
                    <li class="nav-item-list">
                        <a href="<%= request.getContextPath()%>/sabores.html">Sabores</a>
                    </li>
                    <li class="nav-item-list">
                        <a href="<%= request.getContextPath()%>/vistas/momentos.jsp">Momentos</a>
                    </li>
                </ul>
            </div>

            <% if (mostrarBuscador) { %>
            <div class="nav-search">
                <form class="buscador-form" id="form-buscador" onsubmit="return false;">
                    <input type="text" id="input-buscador" placeholder="Buscar producto...">
                    <button type="submit"><i class="fa fa-search"></i></button>
                    <div id="resultados" class="resultados-busqueda"></div>
                </form>
            </div>
            <% } %>
        </nav>
    </div>

    <!-- PERFIL / SESIÓN -->
    <div class="container-buttom">
        <% if (clienteHeader == null) { %>
        <a href="<%= request.getContextPath()%>/vistas/login.jsp" class="btn-sesion">Únete a nosotros</a>
        <% } else { %>
        <div class="perfil-dropdown">
            <button id="btn-perfil" onclick="toggleDropdown()">
                Hola, <%= nombreCorto %> <i class="fa fa-caret-down"></i>
            </button>
            <div id="menu-perfil" class="menu-perfil">
                <a href="<%= request.getContextPath()%>/vistas/perfil.jsp">
                    <i class="fa fa-user"></i> Ver perfil
                </a>
                <a href="${pageContext.request.contextPath}/LogoutController">
                    <i class="fa fa-sign-out-alt"></i> Cerrar sesión
                </a>
            </div>
        </div>
        <% } %>

        <% if (mostrarBotonPedido) { %>
        <a href="<%= request.getContextPath()%>/ProductoControlle?accion=listar" class="btn-pedido">Haz tu pedido</a>
        <% } %>
    </div>
</header>

<script>
    function toggleDropdown() {
        const menu = document.getElementById("menu-perfil");
        if (menu) {
            menu.style.display = (menu.style.display === "block") ? "none" : "block";
        }
    }

    document.addEventListener("click", function (e) {
        const menu = document.getElementById("menu-perfil");
        const btn = document.getElementById("btn-perfil");

        if (menu && btn && !btn.contains(e.target) && !menu.contains(e.target)) {
            menu.style.display = "none";
        }
    });
</script>
