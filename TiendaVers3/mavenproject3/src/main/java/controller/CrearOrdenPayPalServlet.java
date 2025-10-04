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

import service.PayPalService;
import com.paypal.orders.LinkDescription;
import com.paypal.orders.Order;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.Map;

/**
 *
 * @author ASUS
 */
@WebServlet("/crearOrdenPaypal")
public class CrearOrdenPayPalServlet extends HttpServlet {

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
         HttpSession session = request.getSession();

        // 🔹 Recoge parámetros desde el formulario
        String modalidad = request.getParameter("modalidad");
        String montoStr = request.getParameter("monto");

        if (modalidad == null || modalidad.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "❌ Falta el campo 'modalidad'");
            return;
        }

        if (montoStr == null || montoStr.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "❌ Falta el campo 'monto'");
            return;
        }

        double monto;
        try {
            monto = Double.parseDouble(montoStr.trim());
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Monto inválido: " + montoStr);
            return;
        }

        // 🔹 Guarda los datos necesarios en sesión
        session.setAttribute("modalidad", modalidad);
        session.setAttribute("monto_total", monto);

        // 🔸 Guarda también el carrito
        Map<Integer, Integer> carrito = (Map<Integer, Integer>) session.getAttribute("carrito");
        if (carrito == null || carrito.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "❌ Carrito vacío o no encontrado.");
            return;
        }

        session.setAttribute("carrito", carrito); // por si acaso no estaba ya

        // 🔹 Construir URLs
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        String returnUrl = baseUrl + "/confirmarPago";  // este es CapturarPagoServlet
        String cancelUrl = baseUrl + "/errorPago.jsp";  // opcional

        try {
            PayPalService paypal = new PayPalService();
            Order orden = paypal.createOrder(monto, returnUrl, cancelUrl);

            // 🔹 Redirige a PayPal (approve link)
            for (LinkDescription link : orden.links()) {
                if ("approve".equals(link.rel())) {
                    response.sendRedirect(link.href());
                    return;
                }
            }

            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "❌ No se pudo obtener enlace de aprobación de PayPal.");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "❌ Error al crear la orden PayPal.");
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
