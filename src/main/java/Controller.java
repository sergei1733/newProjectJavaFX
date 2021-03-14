import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import domain.Users;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private ComboBox cmbUserId;
    @FXML
    private TextField userName;
    @FXML
    private TextField userEmail;
    @FXML
    private TextField userPhone;
    @FXML
    private TextField userWebsite;
    @FXML
    private TextArea userCompany;
    @FXML
    private TextArea userAddress;


    @FXML
    void Select() {
        cmbUserId.getSelectionModel().getSelectedItem().toString();

        Users users = (Users) cmbUserId.getSelectionModel().getSelectedItem();

        userName.setText("Имя:  " + users.getName());
        userEmail.setText("Email:  " + users.getEmail());
        userPhone.setText("Телефон:  " + users.getPhone());
        userWebsite.setText("Сайт:  " + users.getWebsite());
        userCompany.setText("Компания: Name - " + users.getCompany().getName() +
                ", Catch phrase - " + users.getCompany().getCatchPhrase() +
                ", bs - " + users.getCompany().getBs());
        userAddress.setText("Адрес: Street - " + users.getAddress().getCity() +
                ", Suite - " + users.getAddress().getSuite() +
                ", City - " + users.getAddress().getCity() +
                ", Zipcode - " + users.getAddress().getZipcode());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response =
                restTemplate.getForEntity("https://jsonplaceholder.typicode.com/users", String.class);

        InputStream is = new ByteArrayInputStream(response.getBody().getBytes());

        List list1 = getUsers(is);

        ObservableList<Users> list = FXCollections.observableList(list1);
        cmbUserId.setItems(list);
    }

    public static List<Users> getUsers(InputStream inputStream) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            TypeFactory typeFactory = objectMapper.getTypeFactory();
            CollectionType collectionType = typeFactory.constructCollectionType(
                    List.class, Users.class);
            return objectMapper.readValue(inputStream, collectionType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
