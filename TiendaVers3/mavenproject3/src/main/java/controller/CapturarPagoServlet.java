/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.paypal.orders.Order;
import dao.PedidoDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Map;
import model.Cliente;
import service.PayPalService;

/**
 *
 * @author ASUS
 */
public class CapturarPagoServlet extends HttpServlet {

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
            out.println("<title>Servlet CapturarPagoServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CapturarPagoServlet at " + request.getContextPath() + "</h1>");
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
         String token = request.getParameter("token"); // ID de orden de PayPal

        if (token == null || token.isEmpty()) {
            System.out.println("❌ Token de PayPal no recibido.");
            response.sendRedirect("errorPago.jsp");
            return;
        }

        // Captura la orden desde PayPal
        PayPalService service = new PayPalService();
        Order ordenCapturada = service.captureOrder(token);

        if ("COMPLETED".equals(ordenCapturada.status())) {
            HttpSession session = request.getSession();

            // Obtiene datos necesarios desde la sesión
            Cliente cliente = (Cliente) session.getAttribute("cliente");
            String modalidad = (String) session.getAttribute("modalidad");
            Double monto = (Double) session.getAttribute("monto_total");
            Map<Integer, Integer> carrito = (Map<Integer, Integer>) session.getAttribute("carrito");

            // Log de depuración
            System.out.println("✅ Pago completado. Registrando pedido...");
            System.out.println("Cliente: " + cliente);
            System.out.println("Modalidad: " + modalidad);
            System.out.println("Monto: " + monto);
            System.out.println("Carrito: " + carrito);

            // Validar datos
            if (cliente == null || modalidad == null || monto == null || carrito == null) {
                System.out.println("❌ Faltan datos en sesión. No se puede registrar el pedido.");
                response.sendRedirect("errorPago.jsp");
                return;
            }

            // Registrar el pedido
            PedidoDAO pedidoDAO = new PedidoDAO();
            pedidoDAO.registrarPedido(cliente.getIdCliente(), modalidad, monto, carrito, "PAYPAL");

            // Limpia sesión
            session.removeAttribute("carrito");
            session.removeAttribute("modalidad");
            session.removeAttribute("monto_total");

            // Redirige a confirmación
            response.sendRedirect("vistas/producto/ConfirmacionPago.jsp");
        } else {
            System.out.println("❌ Estado del pago no es COMPLETED. Estado: " + ordenCapturada.status());
            response.sendRedirect("errorPago.jsp");
        }
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
        processRequest(request, response);
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
