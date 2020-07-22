package ec.edu.ups.controlador;

import ec.edu.ups.ejb.BodegaFacade;
import ec.edu.ups.ejb.CategoriaFacade;
import ec.edu.ups.ejb.ProductoFacade;
import ec.edu.ups.ejb.StockFacade;
import ec.edu.ups.entidad.Bodega;
import ec.edu.ups.entidad.Categoria;
import ec.edu.ups.entidad.Producto;
import ec.edu.ups.entidad.Stock;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.annotation.FacesConfig;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.Cookie;
import java.io.Serializable;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@FacesConfig(version = FacesConfig.Version.JSF_2_3)
@Named
@SessionScoped
public class ProductoBean implements Serializable {

    private static final long serialVersionUID=1L;
    @EJB
    private ProductoFacade ejbProductoFacade;
    private String nombre;
    private String imagen;
    private String precioCompra;
    private String precioVenta;
    private String iva;
    private String stock;
    private String categoria;
    private List<Categoria> list;
    private List<Bodega> bodegas;
    private List<String> selectedbodegas;
    @EJB
    private BodegaFacade ejbBodegaFacade;

    @EJB
    private CategoriaFacade ejbCategoriaFacade;

    private List<Producto> productos;
    private String selectedProducto;
    private String stock_mas;
    private List<String> bodegas_stock;
    //atributo para consultar inventario
    private String bodega_inventario;
    private List<Producto> productos_list;
    private boolean disabled=true;
    @EJB
    private StockFacade ejbStockFacade;
    private String cookie;



    public ProductoBean() {

    }

    @PostConstruct
    public void init(){
        list=ejbCategoriaFacade.findAll();
        bodegas= ejbBodegaFacade.findAll();
        productos= ejbProductoFacade.findAll();

    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getBodega_inventario() {
        return bodega_inventario;
    }

    public void setBodega_inventario(String bodega_inventario) {
        this.bodega_inventario = bodega_inventario;
        this.disabled=false;
    }


    public List<String> getBodegas_stock() {
        return bodegas_stock;
    }

    public void setBodegas_stock(List<String> bodegas_stock) {
        this.bodegas_stock = bodegas_stock;
    }


    public String getStock_mas() {
        return stock_mas;
    }

    public void setStock_mas(String stock_mas) {
        this.stock_mas = stock_mas;
    }

    public String getSelectedProducto() {
        return selectedProducto;
    }

    public void setSelectedProducto(String selectedProducto) {
        this.selectedProducto = selectedProducto;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public List<Bodega> getBodegas() {
        return bodegas;
    }

    public void setBodegas(List<Bodega> bodegas) {
        this.bodegas = bodegas;
    }

    public List<String> getSelectedbodegas() {
        return selectedbodegas;
    }

    public void setSelectedbodegas(List<String> selectedbodegas) {
        this.selectedbodegas = selectedbodegas;
    }

    public Categoria[] getList() {
        return list.toArray(new Categoria[0]);
    }

    public void setList(List<Categoria> list) {
        this.list = list;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(String precioCompra) {
        this.precioCompra = precioCompra;
    }

    public String getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(String precioVenta) {
        this.precioVenta = precioVenta;
    }

    public String getIva() {
        return iva;
    }

    public void setIva(String iva) {
        this.iva = iva;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public List<Producto> getProductos_list() {
        return consultarInventarioPorBodega();
    }

    public void setProductos_list(List<Producto> productos_list) {
        this.productos_list = productos_list;
    }

    public void addProducto() {

        char iva_char = iva.charAt(0);

        List<Bodega> bodegas_buscadas = new ArrayList<Bodega>();

        Categoria categoria_buscada = ejbCategoriaFacade.buscarCategoriaPorNombre(categoria);
        System.out.println("aki");
        Producto s1 = new Producto(nombre, imagen, Double.parseDouble(precioCompra), Double.parseDouble(precioVenta), iva_char, Integer.parseInt(stock), categoria_buscada);
        System.out.println("aki2");
        System.out.println("aki3");

        for (String bodega_name : selectedbodegas) {
            String stock_val=this.stock;
            Bodega a1 = ejbBodegaFacade.buscarBodegaPorNombre(bodega_name);
            if (a1 != null) {
                Stock stock = new Stock(Integer.parseInt(stock_val),s1,a1);
                a1.agregarProducto(s1);
                a1.addStock(stock);
                System.out.println("aki4");
                s1.addBodega(a1);
                System.out.println("akifinal");
                s1.addStock(stock);
                System.out.println("salio bien mijin");
            } else {
                System.out.println("el objeto es nulo");
            }
        }
        ejbProductoFacade.create(s1);
    }
    public void aumentarStock(){
        //Buscar el producto
        Producto product=ejbProductoFacade.buscarPrductoPorNombre(selectedProducto);
        if(product!=null)
            System.out.println("PRODUCTO ENCONTRADO");
        //Buscar la Bodega
        Bodega bodeg= ejbBodegaFacade.buscarBodegaPorNombre(bodegas_stock.get(0));
        if(bodeg!=null)
            System.out.println("BODEGA ENCONTRADO");
        //Actualizar entidad Stock
        Stock stock_actualizar = ejbStockFacade.recuperarStock(product,bodeg);
        stock_actualizar.setStock(Integer.parseInt(stock_mas));
        if(stock_actualizar!=null)
            System.out.println("STOCK ENCONTRADO");
        ejbStockFacade.edit(stock_actualizar);
    }
    public List<Producto> consultarInventarioPorBodega(){
        System.out.println("bodega_inventario"+bodega_inventario);
        if(bodega_inventario!=null){
            Bodega bodega_to_inventario=ejbBodegaFacade.buscarBodegaPorNombre(bodega_inventario);
            if(bodega_to_inventario!=null)
                System.out.println("bodega encontrada papi");
            List<Stock> stock_inventario= ejbStockFacade.recuperarStockPorBodega(bodega_to_inventario);
            if(bodega_to_inventario!=null)
                System.out.println("inventario encontrada papi");
            List<Producto> productos_inventario= new ArrayList<Producto>();
            for (Stock stock_inv:stock_inventario
            ) {
                String codigo_prod_inv=stock_inv.getProducto().getNombre();
                System.out.println("nombre del producto buscado "+codigo_prod_inv);

                Producto producto_inv=ejbProductoFacade.buscarPrductoPorNombre(codigo_prod_inv);

                producto_inv.setStock(stock_inv.getStock());
                System.out.println(producto_inv.toString());
                productos_inventario.add(producto_inv);
            }
            return productos_inventario;
        } else {
            Producto pr = new Producto();
            pr.setNombre("No hay");
            List<Producto>productos_null= new ArrayList<Producto>();
            productos_null.add(pr);
            return productos_null;

        }
    }
    public String getCookie() {
        Cookie cookie = (Cookie) FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap().get("administrador");
        if(cookie !=null) {
            String cookieValue = cookie.getValue();
            System.out.println(cookieValue + "<------");
            if (cookieValue.isEmpty()) {
                try {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("/Proyecto/public/logIn.xhtml");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }else{
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/Proyecto/public/logIn.xhtml");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "Bienvenido!";
    }
//FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "paginaBodegas.xhtml")

    public void deleteCookie(){
        System.out.println("METHOD CALLED!");
        FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("administrador", "", null);
        Cookie cookie = (Cookie) FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap().get("administrador");
        if(cookie.getValue().equals("")) System.out.println("Se ha nulificado la cookie de manera correcta!"); else
            System.out.println("Se ha nulificado el valor correctamente!");
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/Proyecto/public/paginaCatalogo.xhtml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void redirectBodegas(){
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/Proyecto/private/paginaBodegas.xhtml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
