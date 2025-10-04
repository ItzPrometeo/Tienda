/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.PedidoDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.Pedido;

/**
 *
 * @author Lenovo
 */
public class GestionPedidosServlet extends HttpServlet {

    private PedidoDAO pedidoDAO;

    @Override
    public void init() {
        pedidoDAO = new PedidoDAO();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet GestionPedidosServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet GestionPedidosServlet at " + request.getContextPath() + "</h1>");
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
       // Cargar pedidos por estado
        List<Pedido> recibidos = pedidoDAO.listarPorEstado("RECIBIDO");
        List<Pedido> enPreparacion = pedidoDAO.listarPorEstado("EN_PREPARACION");
        List<Pedido> enCamino = pedidoDAO.listarPorEstado("EN_CAMINO");
        List<Pedido> entregados = pedidoDAO.listarPorEstado("ENTREGADO");

        // Organizar secciones para el JSP
        @SuppressWarnings("unchecked")
        List<Pedido>[] secciones = new List[]{recibidos, enPreparacion, enCamino, entregados};
        String[] titulos = {"Pedidos Recibidos", "Pedidos en Preparación", "Pedidos en Camino", "Pedidos Entregados"};
        String[] clases = {"border-primary", "border-warning", "border-info", "border-success"};

        // Búsqueda por ID si se envía el parámetro
        Pedido pedidoBuscado = null;
        String idPedidoStr = request.getParameter("idPedido");
        if (idPedidoStr != null && !idPedidoStr.trim().isEmpty()) {
            try {
                int idPedido = Integer.parseInt(idPedidoStr);
                pedidoBuscado = pedidoDAO.buscarPorId(idPedido);
            } catch (NumberFormatException e) {
                // ID inválido, no hacer nada
            }
        }

        // Enviar datos a la vista
        request.setAttribute("secciones", secciones);
        request.setAttribute("titulos", titulos);
        request.setAttribute("clases", clases);
        request.setAttribute("pedidoBuscado", pedidoBuscado);

        request.getRequestDispatcher("/vistas/trabajador/gestion_pedidos.jsp").forward(request, response);
    
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
        doGet(request, response);
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
