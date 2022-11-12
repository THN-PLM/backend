package server.thn.Route.dto.routeProduct;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteProductResponsiblePassDto {

    List<Long> routeIds; // 권한 이양할

    List<Long> memberId; // 권한 받을

}

