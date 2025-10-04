/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import dao.ClienteDAO;
import model.Cliente;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
/**
 *
 * @author ASUS
 */
public class ClienteController extends HttpServlet {

    
    private final ClienteDAO clienteDAO = new ClienteDAO();
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
   protected void actualizarDatos(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    HttpSession session = request.getSession();
    Cliente clienteSesion = (Cliente) session.getAttribute("cliente");

    if (clienteSesion == null) {
        response.sendRedirect(request.getContextPath() + "/vistas/login.jsp");
        return;
    }

    // Obtener los nuevos valores del formulario
    String nuevoNombre = request.getParameter("nombre");
    String nuevoEmail = request.getParameter("correo");
    String nuevoTelefono = request.getParameter("telefono");
    String nuevaDireccion = request.getParameter("direccion");

    // Actualizar objeto en memoria
    clienteSesion.setNombreCompleto(nuevoNombre);
    clienteSesion.setEmail(nuevoEmail);
    clienteSesion.setTelefono(nuevoTelefono);
    clienteSesion.setDireccion(nuevaDireccion);

    // Actualizar en BD
    boolean actualizado = new ClienteDAO().actualizar(clienteSesion);

    if (actualizado) {
        // Volver a guardar en sesión actualizado
        session.setAttribute("cliente", clienteSesion);
    }

    // Redirigir nuevamente a perfil.jsp con ruta correcta
    response.sendRedirect(request.getContextPath() + "/vistas/perfil.jsp");
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
        String accion = request.getParameter("accion");

        if ("actualizar".equals(accion)) {
            actualizarDatos(request, response);
        } else {
            response.sendRedirect("index.jsp");
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
