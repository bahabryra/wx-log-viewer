package com.lesuorac.wx.data;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserPreferences, String> {

}
