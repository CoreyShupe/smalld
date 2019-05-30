package com.github.princesslana.smalld;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestRateLimitBucket {

  @Test
  public void equals_whenSelf_shouldBeTrue() {
    RateLimitBucket bucket = RateLimitBucket.from("GET", "/path");
    Assertions.assertThat(bucket.equals(bucket)).isTrue();
  }

  @Test
  public void equals_whenSameRequest_shouldBeTrue() {
    Assertions.assertThat(
            RateLimitBucket.from("GET", "/path").equals(RateLimitBucket.from("GET", "/path")))
        .isTrue();
  }

  @Test
  public void equals_whenNull_shouldBeFalse() {
    Assertions.assertThat(RateLimitBucket.from("GET", "/path").equals(null)).isFalse();
  }

  @Test
  public void equals_whenString_shouldBeFalse() {
    Assertions.assertThat(RateLimitBucket.from("GET", "/path").equals("/path")).isFalse();
  }

  @Test
  public void from_whenBulkDelete_shouldBeSameBucket() {
    Assertions.assertThat(RateLimitBucket.from("GET", "/channels/123/messages/bulk-delete"))
        .isEqualTo(RateLimitBucket.from("GET", "/channels/123/messages/bulk_delete"));
  }

  @Test
  public void from_whenMessageIdVaries_shouldBeSameBucket() {
    Assertions.assertThat(RateLimitBucket.from("GET", "/channels/123/messages/888"))
        .isEqualTo(RateLimitBucket.from("GET", "/channels/123/messages/999"));
  }

  @Test
  public void from_whenChannelIdVaries_shouldBeDifferentBucket() {
    Assertions.assertThat(RateLimitBucket.from("GET", "/channels/123/messages/999"))
        .isNotEqualTo(RateLimitBucket.from("GET", "/channels/456/messages/999"));
  }

  @Test
  public void from_whenDeleteMessage_shouldBeDifferentToGet() {
    Assertions.assertThat(RateLimitBucket.from("GET", "/channels/123/messages/999"))
        .isNotEqualTo(RateLimitBucket.from("DELETE", "/channels/123/messages/999"));
  }
}