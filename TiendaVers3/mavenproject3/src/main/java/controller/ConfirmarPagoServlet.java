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
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Map;
import model.Cliente;
import model.Producto;
import service.CorreoService;
import service.PayPalService;

/**
 *
 * @author ASUS
 */
@WebServlet("/confirmarPago")
public class ConfirmarPagoServlet extends HttpServlet {

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
            out.println("<title>Servlet ConfirmarPagoServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ConfirmarPagoServlet at " + request.getContextPath() + "</h1>");
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
        String token = request.getParameter("token"); // OrderID de PayPal
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("cliente") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        if (token == null || token.isEmpty()) {
            response.sendRedirect("ConfirmacionPago.jsp?estado_pago=fail");
            return;
        }

        try {
            PayPalService paypalService = new PayPalService();
            Order order = paypalService.captureOrder(token);

            if ("COMPLETED".equalsIgnoreCase(order.status())) {
                // ✅ Registrar pedido si hay sesión válida
                Cliente cliente = (Cliente) session.getAttribute("cliente");
                String modalidad = (String) session.getAttribute("modalidad");
                Double total = (Double) session.getAttribute("monto_total");
                Map<Integer, Integer> carrito = (Map<Integer, Integer>) session.getAttribute("carrito");

                if (cliente != null && carrito != null && total != null) {
                    PedidoDAO dao = new PedidoDAO();
                    dao.registrarPedido(cliente.getIdCliente(), modalidad, total, carrito, "PAYPAL");


                    // Correo
                    String asunto = "🛒 Tu pedido en Tosta Café fue registrado correctamente";
                    StringBuilder cuerpo = new StringBuilder();
                    cuerpo.append("Hola ").append(cliente.getNombreCompleto()).append(",\n\n")
                            .append("Gracias por tu compra en Tosta Café. Estos son los detalles de tu pedido:\n\n")
                            .append("Modalidad: ").append(modalidad).append("\n")
                            .append("Total pagado: S/. ").append(total).append("\n")
                            .append("Productos:\n");

                    for (Map.Entry<Integer, Integer> entry : carrito.entrySet()) {
                        Producto p = new dao.ProductoDAO().obtenerPorId(entry.getKey());
                        if (p != null) {
                            cuerpo.append("- ").append(p.getNombre())
                                    .append(" x ").append(entry.getValue())
                                    .append(" = S/. ").append(p.getPrecio() * entry.getValue())
                                    .append("\n");
                        }
                    }

                    if ("LOCAL".equalsIgnoreCase(modalidad)) {
                        cuerpo.append("\n📍 Puedes recoger tu pedido en 15 minutos.");
                    } else {
                        cuerpo.append("\n🚚 Tu pedido llegará entre 30-45 minutos por delivery.");
                    }

                    cuerpo.append("\n\n¡Gracias por confiar en nosotros!\nTosta Café ☕");

                    CorreoService.enviarCorreoPedido(cliente.getEmail(), asunto, cuerpo.toString());

                    // Limpiar sesión
                    session.removeAttribute("carrito");
                    session.removeAttribute("monto_total");
                    session.removeAttribute("modalidad");
                }

                response.sendRedirect(request.getContextPath() + "/vistas/producto/ConfirmacionPago.jsp"
                        + "?estado_pago=ok"
                        + "&modalidad=" + modalidad
                        + "&total=" + total);

            } else {
                response.sendRedirect(request.getContextPath() + "/vistas/producto/ConfirmacionPago.jsp?estado_pago=fail");

            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/vistas/producto/ConfirmacionPago.jsp?estado_pago=fail");

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
