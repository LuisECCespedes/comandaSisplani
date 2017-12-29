package com.example.fluc.siservis_comanda.data.api;

import com.example.fluc.siservis_comanda.data.modelo.ComandaProductos;
import com.example.fluc.siservis_comanda.data.modelo.Especificaciones;
import com.example.fluc.siservis_comanda.data.modelo.Mesa;
import com.example.fluc.siservis_comanda.data.modelo.MotivosAnulacion;
import com.example.fluc.siservis_comanda.data.modelo.Productos;
import com.example.fluc.siservis_comanda.data.modelo.TipoProducto;
import com.example.fluc.siservis_comanda.data.modelo.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by fluc on 21/11/2017.
 */

public interface ComandaApi {
    // metodo para login
    @POST("login")
    Call<User> login(@Body User usuario);

    // metodo que listara las mesas
    @GET("motivosListar/{esquema}")
    Call<List<MotivosAnulacion>> listarMotivos(@Path("esquema") String esquema);

    // metodo que listara las mesas
    @GET("mesas/{mesas}")
    Call<List<Mesa>> listarMesas(@Path("mesas") String mesas);

    // metodo que listara los tipos de productos
    @GET("listarTipProd/{esquema}/{like}")
    Call<List<TipoProducto>> listarTipoproductos(@Path("esquema") String esquema, @Path("like") String like);

    // metodo que listara las productos
    @GET("listarProd/{esquema}/{prodTipo}/{like}")
    Call<List<Productos>> listarProductos(@Path("esquema") String esquema, @Path("prodTipo") String prodTipo, @Path("like") String like);

    // metodo que listara las especificaciones
    @GET("listarEspec/{esquema}/{prodId}")
    Call<List<Especificaciones>> listarEspecificaciones(@Path("esquema") String esquema, @Path("prodId") String prodId);

    // metodo que listara las comandas
    @GET("comandaMesa/{esquema}/{mesa}/{usuario}")
    Call<List<ComandaProductos>> comandaMesas(@Path("esquema") String esquema, @Path("mesa") String mesa, @Path("usuario") String usuario);

    // metodo que ingresara el nuevo producto
    @GET("comandaProductoNuevo/{esquema}/{mesa}/{usuario}/{prod_id}/{precio}/{especAcum}/{desc}/{especCod}/{especExt}/{nCantidad}")
    Call<User> comandaNuevoProducto(@Path("esquema") String esquema, @Path("mesa") String mesa, @Path("usuario") String usuario,
    @Path("prod_id") String prod_id, @Path("precio") String precio, @Path("especAcum") String especAcum,
    @Path("desc") String desc, @Path("especCod") String especCod, @Path("especExt") String especExt,@Path("nCantidad") int nCantidad);

    // cerrar nueva comanda
    @GET("comandaCerrar/{esquema}/{cabCodigo}/{userId}")
    Call<User> comandaCerrar(@Path("esquema") String esquema, @Path("cabCodigo") int cabCodigo,@Path("userId") String userId);

    // reimprimir comanda
    @GET("comandaImpReimp/{esquema}/{cabCodigo}/{userId}")
    Call<User> comandaReeImprimir(@Path("esquema") String esquema, @Path("cabCodigo") int cabCodigo,@Path("userId") String userId);

    // reimprimir comanda
    @GET("comandaImpPreCuenta/{esquema}/{srvId}/{userId}")
    Call<User> comandaImprimirPrecuenta(@Path("esquema") String esquema, @Path("srvId") int srvId,@Path("userId") String userId);

    // motivo de cancelacion
    @GET("comandaCancelarItemMotivo/{esquema}/{detCodigo}/{userId}/{notifId}/{codigoProducto}")
    Call<User> comandaCancelarItemMotivo(@Path("esquema") String esquema, @Path("detCodigo") int detCodigo,
                                         @Path("userId") String userId, @Path("notifId") int notifId, @Path("codigoProducto") String codigoProducto);

    // motivo de cancelacion
    @GET("comandaCancelarItemMotivoNuevo/{esquema}/{detCodigo}/{userId}/{notifDescri}/{codigoProducto}")
    Call<User> comandaCancelarItemMotivo(@Path("esquema") String esquema, @Path("detCodigo") int detCodigo,
                                         @Path("userId") String userId, @Path("notifDescri") String notifDescri, @Path("codigoProducto") String codigoProducto);

    // retornar el producto al kardex
    @GET("comandaCancelarItemReintegroKardex/{esquema}/{prodId}/{userId}/{detCodigo}")
    Call<User> comandaCancelarItemReintegroKardex(@Path("esquema") String esquema, @Path("prodId") String prodId,
                                                  @Path("userId") String userId, @Path("detCodigo") int detCodigo);

    // stock de los productos
    @GET("comandaProductoComprobarStock/{esquema}/{cabCodigo}")
    Call<ApiError> productoComprobarStock(@Path("esquema") String esquema, @Path("cabCodigo") int cabCodigo);

/*
    comandaCerrar
    comandaImpReimp
               */

}