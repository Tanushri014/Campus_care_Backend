package campus_care.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//Whenever this exception is thrown, return 404 Not Found automatically.
@ResponseStatus(value= HttpStatus.NOT_FOUND)
public class ResourceNotFoundException  extends  RuntimeException{

    public ResourceNotFoundException(String message){
        super(message);
    }


}
