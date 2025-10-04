package rest;

import dto.PedidoDTO;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import service.VerificadorCorreoZeroBounce;

@Path("/pedido")
public class PedidoResource {

    @POST
    @Path("/confirmar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response confirmarPedido(PedidoDTO pedido) {
        if (!VerificadorCorreoZeroBounce.esCorreoValido(pedido.getCorreo())) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("{\"error\":\"Correo inválido o no entregable\"}")
                           .build();
        }

        // Continuar con lógica de pedido...
        return Response.ok("{\"mensaje\":\"Correo válido y pedido procesado\"}").build();
    }
}
