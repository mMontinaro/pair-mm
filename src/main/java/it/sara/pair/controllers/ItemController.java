import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController 
@RequestMapping ("item/v1/item")
public class ItemController { 
    private static final String BASE_PATH = "item/v1/item"; 

    @PutMapping(BASE_PATH + "/{guid}")
    public void create() {
        // Placeholder for the update item endpoint
    }

    @PostMapping(BASE_PATH + "/{guid}")
    public void update() {
        // Placeholder for the create item endpoint
    }

    @DeleteMapping (BASE_PATH + "/{guid}")
    public void delete() {  
        // Placeholder for the delete item endpoint
    }

    @PostMapping(BASE_PATH + "/search")
    public void search() {
        // Placeholder for the search item endpoint
    }

    @GetMapping(BASE_PATH + "/{guid}")
    public void detail() {
        // Placeholder for the get item detail endpoint
    }
}