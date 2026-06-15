package com.innovation.training.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.innovation.training.module.user.entity.User;
import com.innovation.training.module.user.mapper.UserMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAuthDetailsService implements UserDetailsService {

    private final UserMapper userMapper;

    public UserAuthDetailsService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return loadUserByUsernameAndTeacherType(username, null);
    }

    public UserDetails loadUserByUsernameAndTeacherType(String username, String teacherType)
            throws UsernameNotFoundException {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username)
                .eq(teacherType != null && !teacherType.isBlank(), User::getTeacherType, teacherType)
                .orderByAsc(User::getId)
                .last("LIMIT 1"));
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(List.of(
                        new SimpleGrantedAuthority("ROLE_" + user.getRole()),
                        new SimpleGrantedAuthority("TEACHER_TYPE_" + user.getTeacherType())
                ))
                .disabled(user.getStatus() == null || user.getStatus() == 0)
                .build();
    }
}
