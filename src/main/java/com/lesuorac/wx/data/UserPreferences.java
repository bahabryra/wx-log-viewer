package com.lesuorac.wx.data;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Size;

@Entity
public class UserPreferences {

    @Id
    @Size(max = 30, min = 1)
    @Column(nullable = false)
    private String username;

    @ElementCollection
    private List<String> blockedHostnames;

    @ElementCollection
    private List<String> blockedAddresses;

    @ElementCollection
    private List<Long> blockedAsns;

    @Deprecated
    public UserPreferences() {
    }

    public UserPreferences(String username) {
        super();
        this.username = username;
        this.blockedHostnames = new ArrayList<>();
        this.blockedAddresses = new ArrayList<>();
        this.blockedAsns = new ArrayList<>();
    }

    /**
     * @return the username
     * @see #bare_field_name
     */
    public final String getUsername() {
        return this.username;
    }

    /**
     * @param username
     *            the username to set
     * @see #bare_field_name
     */
    public final void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the blockedHostnames
     * @see #bare_field_name
     */
    public final List<String> getBlockedHostnames() {
        return this.blockedHostnames;
    }

    /**
     * @param blockedHostnames
     *            the blockedHostnames to set
     * @see #bare_field_name
     */
    public final void setBlockedHostnames(List<String> blockedHostnames) {
        this.blockedHostnames = blockedHostnames;
    }

    /**
     * @return the blockedAddresses
     * @see #bare_field_name
     */
    public final List<String> getBlockedAddresses() {
        return this.blockedAddresses;
    }

    /**
     * @param blockedAddresses
     *            the blockedAddresses to set
     * @see #bare_field_name
     */
    public final void setBlockedAddresses(List<String> blockedAddresses) {
        this.blockedAddresses = blockedAddresses;
    }

    /**
     * @return the blockedAsns
     * @see #bare_field_name
     */
    public final List<Long> getBlockedAsns() {
        return this.blockedAsns;
    }

    /**
     * @param blockedAsns
     *            the blockedAsns to set
     * @see #bare_field_name
     */
    public final void setBlockedAsns(List<Long> blockedAsns) {
        this.blockedAsns = blockedAsns;
    }

}
