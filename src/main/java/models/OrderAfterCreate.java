package models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class OrderAfterCreate {

    private Ingredient[] ingredients;
    private String _id;
    private User owner;
    private String status;
    private String name;
    private String createdAt;
    private String updatedAt;
    private int number;
    private int price;

}
