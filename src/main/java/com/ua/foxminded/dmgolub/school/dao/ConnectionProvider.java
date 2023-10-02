package com.ua.foxminded.dmgolub.school.dao;

import java.sql.Connection;

public interface ConnectionProvider {

    Connection get();
}