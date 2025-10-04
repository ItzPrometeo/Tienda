/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.ProductoDAO;
import model.Producto;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author ASUS
 */
@WebServlet(name = "ProductoControlle", urlPatterns = {"/ProductoControlle"})
public class ProductoControlle extends HttpServlet {

    ProductoDAO dao = new ProductoDAO();

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ProductoControlle</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ProductoControlle at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        if (accion == null || accion.isEmpty()) {
            accion = "comprar"; // <- fuerza mostrar productos
        }
        switch (accion) {
            case "nuevo":
                request.getRequestDispatcher("/vistas/producto/nuevo.jsp").forward(request, response);
                break;

            case "editar":
                int idEditar = Integer.parseInt(request.getParameter("id"));
                Producto productoEditar = dao.obtenerPorId(idEditar);
                request.setAttribute("producto", productoEditar);
                request.getRequestDispatcher("/vistas/producto/editar.jsp").forward(request, response);
                break;

            case "eliminar":
                int idEliminar = Integer.parseInt(request.getParameter("id"));
                dao.eliminar(idEliminar);
                response.sendRedirect("ProductoController?accion=comprar");
                break;

            case "listar":
                request.setAttribute("lista", dao.listar());
                request.getRequestDispatcher("/vistas/producto/listar.jsp").forward(request, response);
                break;
            case "buscar":
                String q = request.getParameter("q");
                List<Producto> resultados = dao.buscarPorNombre(q);
                request.setAttribute("lista", resultados);
                request.getRequestDispatcher("/vistas/producto/listar.jsp").forward(request, response);
                break;
            default:
                request.setAttribute("lista", dao.listar());
                request.getRequestDispatcher("/vistas/producto/listar.jsp").forward(request, response);
                break;
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        if ("insertar".equals(accion)) {
            Producto p = new Producto();
            p.setNombre(request.getParameter("nombre"));
            p.setPrecio(Double.parseDouble(request.getParameter("precio")));
            p.setUrlImagen(request.getParameter("url_imagen"));
            p.setStock(Integer.parseInt(request.getParameter("stock")));
            p.setIdCategoria(Integer.parseInt(request.getParameter("id_categoria")));
            dao.agregar(p);
        } else if ("actualizar".equals(accion)) {
            Producto p = new Producto();
            p.setIdProducto(Integer.parseInt(request.getParameter("id")));
            p.setNombre(request.getParameter("nombre"));
            p.setPrecio(Double.parseDouble(request.getParameter("precio")));
            p.setUrlImagen(request.getParameter("url_imagen"));
            p.setStock(Integer.parseInt(request.getParameter("stock")));
            p.setIdCategoria(Integer.parseInt(request.getParameter("id_categoria")));
            dao.actualizar(p);
        }

        response.sendRedirect("/vistas/producto/listar.jsp");

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
