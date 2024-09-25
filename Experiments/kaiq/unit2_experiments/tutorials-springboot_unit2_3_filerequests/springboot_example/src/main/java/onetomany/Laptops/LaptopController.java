package onetomany.Laptops;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 
 * @author Vivek Bengre
 * 
 */ 

@RestController
public class LaptopController {

    // This is path to which the file will be written
    // You can optionally store it the database if the files
    // are of the range of 1MB. Larger files slow down the 
    // database query speeds over time
    private static String uploadPath = "/uploadedFiles/";

    @Autowired
    LaptopRepository laptopRepository;

    @Autowired
    HttpServletRequest httpServletRequest;
    
    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/laptops")
    List<Laptop> getAllLaptops(){
        return laptopRepository.findAll();
    }

    @GetMapping(path = "/laptops/{id}")
    Laptop getLaptopById(@PathVariable int id){
        return laptopRepository.findById(id);
    }

    @PostMapping(path = "/laptops")
    String createLaptop(@RequestBody Laptop Laptop){
        if (Laptop == null)
            return failure;
        laptopRepository.save(Laptop);
        return success;
    }

    @PutMapping(path = "/laptops/{id}")
    Laptop updateLaptop(@PathVariable int id, @RequestBody Laptop request){
        Laptop laptop = laptopRepository.findById(id);
        if(laptop == null)
            return null;
        laptopRepository.save(request);
        return laptopRepository.findById(id);
    }

    @DeleteMapping(path = "/laptops/{id}")
    String deleteLaptop(@PathVariable int id){

        Laptop laptop = laptopRepository.findById(id);

        if(laptop == null)
            return success;
        
        // delete associated file of object is deleted
        File currentInvoice = new File(laptop.getInvoicePath());
        if (currentInvoice != null && currentInvoice.exists())
            currentInvoice.delete();
        
        laptopRepository.deleteById(id);
        return success;
    }

     /**
     * This is where the invoice is assigned to each laptop, you can observe that there is no @RequestBody,
     * this is because a file and data cannot be uploaded together with different formats with @RequestBody.
     * @RequestParam allows for 2 different types of data i.e. file and text. Only the file is upoaded in this case
     * and has the key 'invoice'. Note that in this case the file is being saved to the disk and the path is stored in
     * database. Using this path, one can access the files. While testing with postman use form-data as body and key as invoice
     */
    @PutMapping(path = "/laptops/{id}/invoice")
    public String uploadLaptopInvoice(@PathVariable int id, @RequestParam("invoice") MultipartFile invoiceFile) throws IOException{
        if(invoiceFile.isEmpty()) 
            return failure;
        
        // get the complete path from the root to save file
        String fullPath = httpServletRequest.getServletContext().getRealPath(uploadPath);

        // creates a new directory if it doesnt exist
        if (!new File(fullPath).exists())
            new File(fullPath).mkdir();
        
        // save the file in such a way that it can be uniquely identified even if the uploaded file is the same
        fullPath+=("laptop_invoice_"+id+"_"+invoiceFile.getOriginalFilename());
        System.out.println(fullPath);
        // get the Laptop associated with the id
        Laptop laptop = laptopRepository.findById(id);
        String currentPath = laptop.getInvoicePath();

        // delete if there is an invoice present already
        if (currentPath != null){        
            File currentInvoice = new File(currentPath);
            if(currentInvoice.exists())
                currentInvoice.delete();
        }

        // associate the newly uploaded invoice with the laptop
        // and save the new path in the database
        laptop.setInvoicePath(fullPath);
        laptopRepository.save(laptop);

        // save the file to disk
        File tempFile = new File(fullPath);
        invoiceFile.transferTo(tempFile);
        
        return success;
    }

     /**
     * The Response Entity type is set as <Resource>, which can handle files and images very well
     * additional header have to be set to tell the front end what type of conent is being sent from the 
     * backend, thus in this example headers are set.
    */
    @GetMapping(path = "/laptops/{id}/invoice")
    ResponseEntity<Resource> getLaptopInvoice(@PathVariable int id) throws IOException{
        
        Laptop laptop = laptopRepository.findById(id);

        // if laptop id not found it cannot have an invoice associated with it
        if(laptop == null || laptop.getInvoicePath() == null)
            return null;
        
        // Check the file if it exists and load it into the memory
        File file = new File(laptop.getInvoicePath());
        if(!file.exists())
            return null;

        String[] splitPath = laptop.getInvoicePath().split("/");
        String fileName = splitPath[splitPath.length-1];
        // add headers to state that a file is being downloaded
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+fileName);
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        // convert the file into bytearrayresource format to send to the front end with the file
        Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource data = new ByteArrayResource(Files.readAllBytes(path));

        // send the response entity back to the front end with the 
        return ResponseEntity.ok()
                .headers(header)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(data);
    }
}
