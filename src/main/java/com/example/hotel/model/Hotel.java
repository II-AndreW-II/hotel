package com.example.hotel.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "hotels")
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;

    private String description;

    private String brand;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "contact_id")
    private Contact contacts;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "arrival_time_id")
    private ArrivalTime arrivalTime;

    @ManyToMany
    @JoinTable(
        name = "hotel_amenities",
        joinColumns = @JoinColumn(name = "hotel_id"),
        inverseJoinColumns = @JoinColumn(name = "amenity_id")
    )

    private List<Amenity> amenities = new ArrayList<>();
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }
    public Contact getContacts() { return contacts; }
    public void setContacts(Contact contacts) { this.contacts = contacts; }
    public ArrivalTime getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(ArrivalTime arrivalTime) { this.arrivalTime = arrivalTime; }
    public List<Amenity> getAmenities() { return amenities; }
    public void setAmenities(List<Amenity> amenities) { this.amenities = amenities; }
}
