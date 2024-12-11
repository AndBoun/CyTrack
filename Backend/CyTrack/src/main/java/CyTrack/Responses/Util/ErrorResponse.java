package CyTrack.Responses.Util;

public class ErrorResponse {
    private String status;
    private error error;

    public ErrorResponse(String status, int code, String message, String details){
        this.status = status;
        this.error = new error(code, message, details);
    }
    public String getStatus(){
        return status;
    }
    public error getError(){
        return error;
    }
    public void setStatus(String status){
        this.status = status;
    }
    public void setError(error error){
        this.error = error;
    }



    public static class error{
        private int code;
        private String message;
        private String details;

        public error(int code, String message, String details){
            this.code = code;
            this.message = message;
            this.details = details;
        }
        public int getCode(){
            return code;
        }
        public String getMessage(){
            return message;
        }
        public String getDetails(){
            return details;
        }
        public void setCode(int code){
            this.code = code;
        }
        public void setMessage(String message){
            this.message = message;
        }
        public void setDetails(String details){
            this.details = details;
        }

    }
}
