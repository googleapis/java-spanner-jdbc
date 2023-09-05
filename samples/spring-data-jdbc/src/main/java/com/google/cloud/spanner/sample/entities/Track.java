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

package com.google.cloud.spanner.sample.entities;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("tracks")
public class Track extends AbstractEntity {

  /**
   * We need to map this to the ID column to be able to explicitly set it, instead of letting Spring
   * Data generate it. This is necessary, because Track is interleaved in Album. That again means
   * that we must use the ID value of the Album for a Track.
   */
  @Column("id")
  private Long albumId;

  private int trackNumber;

  private String title;

  private Double sampleRate;

  public Track(Album album, int trackNumber, String title) {
    setAlbumId(album.getId());
    this.trackNumber = trackNumber;
    this.title = title;
  }

  public Long getAlbumId() {
    return albumId;
  }

  private void setAlbumId(Long albumId) {
    this.albumId = albumId;
  }

  public int getTrackNumber() {
    return trackNumber;
  }

  public void setTrackNumber(int trackNumber) {
    this.trackNumber = trackNumber;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Double getSampleRate() {
    return sampleRate;
  }

  public void setSampleRate(Double sampleRate) {
    this.sampleRate = sampleRate;
  }
}
