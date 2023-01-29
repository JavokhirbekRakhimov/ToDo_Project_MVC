package uz.ovir.ovir_project.config.annotation;
import uz.ovir.ovir_project.entity.enums.RoleEnum;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckRole {
    RoleEnum[] value();
}
