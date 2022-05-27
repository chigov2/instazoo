package com.chigovv.instazoo.repository;

import com.chigovv.instazoo.entity.ImageModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<ImageModel, Long> {

    Optional<ImageModel> findByUserId(Long userId);//вернуть фото, которое принадлежит юзеру

    Optional<ImageModel> findByPostId(Long postId); //найдем фото для поста
}

//созданы все интерфейсы, которые будут использоваться, чтобы искать данные в БД