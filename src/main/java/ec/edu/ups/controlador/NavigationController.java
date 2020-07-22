package ec.edu.ups.controlador;


import javax.enterprise.context.SessionScoped;
import javax.faces.annotation.FacesConfig;
import javax.faces.annotation.ManagedProperty;
import javax.inject.Named;
import java.io.Serializable;

@FacesConfig(version = FacesConfig.Version.JSF_2_3)
@Named
@SessionScoped
public class NavigationController implements Serializable {

    @ManagedProperty(value = "#{param.pageId}")
    private String pageId;

    public String showPage(){
        switch (pageId){
            case "login":
                return "logIn";

            default:
                return "/Proyecto/public/paginaCatalogo";
        }
    }

    public String processLogin(){
        return "/Proyecto/public/paginaAdministrador";
    }

}
