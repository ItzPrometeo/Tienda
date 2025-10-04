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

import dao.ClienteDAO;
import model.Cliente;
import service.VerificadorCorreoZeroBounce;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 *
 * @author ASUS
 */
@WebServlet(name = "RegistroController", urlPatterns = {"/RegistroController"})
public class RegistroController extends HttpServlet {

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
            out.println("<title>Servlet RegistroController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet RegistroController at " + request.getContextPath() + "</h1>");
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
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    String nombre = request.getParameter("nombre_completo");
    String telefono = request.getParameter("telefono");
    String email = request.getParameter("email");
    String direccion = request.getParameter("direccion");

    // ✅ Verifica el correo antes de continuar
    if (!VerificadorCorreoZeroBounce.esCorreoValido(email)) {
        request.setAttribute("error", "El correo ingresado no es válido o no puede recibir mensajes.");
        request.getRequestDispatcher("/vistas/registro.jsp").forward(request, response); // Ruta corregida
        return;
    }

    Cliente cliente = new Cliente();
    cliente.setNombreCompleto(nombre);
    cliente.setTelefono(telefono);
    cliente.setEmail(email);
    cliente.setDireccion(direccion);

    ClienteDAO dao = new ClienteDAO();

    if (dao.registrar(cliente)) {
        Cliente registrado = dao.obtenerPorEmailYTelefono(email, telefono);
        HttpSession session = request.getSession();
        session.setAttribute("cliente", registrado);
        response.sendRedirect(request.getContextPath() + "/ProductoControlle"); // ✅ Redirección segura
    } else {
        request.setAttribute("error", "Error al registrar. Intente nuevamente.");
        request.getRequestDispatcher("/vistas/registro.jsp").forward(request, response); // Mostrar error en el registro
    }
}

    
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
