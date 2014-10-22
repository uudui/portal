package com.nx.domain.security;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Neal on 10/12 012.
 */
@Entity
public class Resource {
    @Id
    private Long id;

    @Column
    private String name;

    @Enumerated(value = EnumType.STRING)
    private ResourceType Type;

    @Column
    private int priority;

    @Column
    private String permission;

    @Column
    private boolean available;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Resource parent;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parent")
    private List<Resource> children;

    @ManyToMany(cascade = CascadeType.REFRESH, mappedBy = "resources")
    private List<Role> roles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ResourceType getType() {
        return Type;
    }

    public void setType(ResourceType type) {
        Type = type;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Resource getParent() {
        return parent;
    }

    public void setParent(Resource parent) {
        this.parent = parent;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public List<Resource> getChildren() {
        return children;
    }

    public void setChildren(List<Resource> children) {
        this.children = children;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Resource)) return false;

        Resource resource = (Resource) o;

        if (id != null ? !id.equals(resource.id) : resource.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
