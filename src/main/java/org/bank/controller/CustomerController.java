package org.bank.controller;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bank.model.Customer;
import org.bank.service.CustomerService;
import org.bank.serviceImpl.CustomerServiceImpl;

import java.util.List;
import java.util.Map;

@Path("/customer")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerController {

    private final CustomerService service = new CustomerServiceImpl();

    // POST -> onboard a new customer
    @POST
    @Path("/addcustomer")
    public Response addCustomer(Customer customer) {
        try {
            Customer savedCustomer = service.addCustomer(customer);
            return Response.status(Response.Status.CREATED).entity(Map.of(
                    "message", "Customer added successfully!",
                    "customer", savedCustomer
            )).build();
        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of(
                    "error", ex.getMessage()
            )).build();
        } catch (RuntimeException ex) {
            // Handles DB errors like Duplicate, Null violations
            return Response.status(Response.Status.CONFLICT).entity(Map.of(
                    "error", ex.getMessage()
            )).build();
        }
    }

    // GET -> fetch customer by ID (ID from JSON body)
    @GET
    @Path("/getcustomerbyid")
    public Response getCustomerById(Customer customer) {
        if (customer == null || customer.getCustomerId() <= 0) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of(
                    "error", "Customer ID must be provided in JSON body"
            )).build();
        }

        Customer fetchedCustomer = service.findById(customer.getCustomerId());
        if (fetchedCustomer != null) {
            return Response.ok(fetchedCustomer).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity(Map.of(
                    "error", "Customer not found with ID: " + customer.getCustomerId()
            )).build();
        }
    }


    // GET -> fetch all customers
    @GET
    @Path("/getallcustomer")
    public Response getAllCustomers() {
        List<Customer> customers = service.findAll();
        return Response.ok(customers).build();
    }

    // PUT -> update customer details
    @PUT
    @Path("/updatecustomer")
    public Response updateCustomer(Customer customer) {
        try {
            boolean result = service.updateCustomer(customer);
            if (result) {
                return Response.ok(Map.of(
                        "message", "Customer updated successfully"
                )).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity(Map.of(
                        "error", "Customer not found"
                )).build();
            }
        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of(
                    "error", ex.getMessage()
            )).build();
        } catch (RuntimeException ex) {
            return Response.status(Response.Status.CONFLICT).entity(Map.of(
                    "error", ex.getMessage()
            )).build();
        }
    }

    // DELETE -> remove customer (ID from JSON body)
    @DELETE
    @Path("/deletecustomer")
    public Response deleteCustomer(Customer customer) {
        if (customer == null || customer.getCustomerId() <= 0) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of(
                    "error", "Customer ID must be provided in JSON body"
            )).build();
        }

        boolean result = service.deleteCustomer(customer.getCustomerId());
        if (result) {
            return Response.ok(Map.of(
                    "message", "Customer deleted successfully"
            )).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity(Map.of(
                    "error", "Customer not found with ID: " + customer.getCustomerId()
            )).build();
        }
    }

}
