package model;

import java.util.Date;

public class Pago {
    private int idPago;
    private int idPedido;
    private Date fechaPago;
    private double monto;
    private String referenciaTrans;

    // Getters y setters...

    public int getIdPago() {
        return idPago;
    }

    public void setIdPago(int idPago) {
        this.idPago = idPago;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getReferenciaTrans() {
        return referenciaTrans;
    }

    public void setReferenciaTrans(String referenciaTrans) {
        this.referenciaTrans = referenciaTrans;
    }
    
}
