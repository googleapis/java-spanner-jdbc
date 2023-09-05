/*
 * Copyright 2023 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.cloud.spanner.sample.service;

import com.google.cloud.spanner.sample.entities.Album;
import com.google.cloud.spanner.sample.entities.Singer;
import com.google.cloud.spanner.sample.repositories.AlbumRepository;
import com.google.cloud.spanner.sample.repositories.SingerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SingerService {
  private final SingerRepository singerRepository;

  private final AlbumRepository albumRepository;

  public SingerService(SingerRepository singerRepository, AlbumRepository albumRepository) {
    this.singerRepository = singerRepository;
    this.albumRepository = albumRepository;
  }

  /** Creates a singer and a list of albums in a transaction. */
  @Transactional
  public Singer createSingerAndAlbums(Singer singer, Album... albums) {
    singer = singerRepository.save(singer);
    for (Album album : albums) {
      album.setSingerId(singer.getId());
      albumRepository.save(album);
    }
    return singer;
  }
}
