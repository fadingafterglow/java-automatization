package ua.edu.ukma.fin.userinfo.model.[:main-sd:];

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.edu.ukma.fin.common.helpers.GettableById;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class [:main-h-ns:]View implements Serializable, GettableById<Integer> {
    private static final long serialVersionUID = -9223372036854775808L;

    private Integer id;
    private String userEmail;

}