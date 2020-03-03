package de.slothsoft.mp4spliterator.core;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SectionTest {

	private VideoPart firstPart;
	private Section section;

	@Before
	public void setUp() throws Exception {
		this.firstPart = new Chapter("1").startTime(1234).endTime(5678);
		this.section = new Section(this.firstPart);
	}

	@Test
	public void testGetTitle() {
		Assert.assertEquals(this.firstPart.getTitle(), this.section.getTitle());

		final VideoPart secondPart = new Chapter("2A").startTime(5678).endTime(6789);
		this.section.addParts(secondPart);

		Assert.assertEquals("1, 2A", this.section.getTitle());

		final VideoPart thirdPart = new Chapter("3A").startTime(0).endTime(1234);
		this.section.addPart(thirdPart);

		Assert.assertEquals("3A, 1, 2A", this.section.getTitle());
	}

	@Test
	public void testGetStartTime() {
		Assert.assertEquals(this.firstPart.getStartTime(), this.section.getStartTime());

		final VideoPart secondPart = new Chapter("2B").startTime(5678).endTime(6789);
		this.section.addPart(secondPart);

		Assert.assertEquals(this.firstPart.getStartTime(), this.section.getStartTime());

		final VideoPart thirdPart = new Chapter("3B").startTime(0).endTime(1234);
		this.section.addPart(thirdPart);

		Assert.assertEquals(thirdPart.getStartTime(), this.section.getStartTime());
	}

	@Test
	public void testGetEndTime() {
		Assert.assertEquals(this.firstPart.getEndTime(), this.section.getEndTime());

		final VideoPart secondPart = new Chapter("2C").startTime(5678).endTime(6789);
		this.section.addPart(secondPart);

		Assert.assertEquals(secondPart.getEndTime(), this.section.getEndTime());

		final VideoPart thirdPart = new Chapter("3C").startTime(0).endTime(1234);
		this.section.addPart(thirdPart);

		Assert.assertEquals(secondPart.getEndTime(), this.section.getEndTime());
	}

	@Test
	public void testGetParts() {
		Assert.assertArrayEquals(new VideoPart[]{this.firstPart}, this.section.getParts());

		final VideoPart secondPart = new Chapter("2D").startTime(5678).endTime(6789);
		this.section.addPart(secondPart);

		Assert.assertArrayEquals(new VideoPart[]{this.firstPart, secondPart}, this.section.getParts());

		final VideoPart thirdPart = new Chapter("3D").startTime(0).endTime(1234);
		this.section.addPart(thirdPart);

		Assert.assertArrayEquals(new VideoPart[]{thirdPart, this.firstPart, secondPart}, this.section.getParts());
	}

	@Test
	public void testContainsPart() {
		Assert.assertTrue(this.section.containsPart(this.firstPart));

		final VideoPart secondPart = new Chapter("2E").startTime(5678).endTime(6789);
		Assert.assertFalse(this.section.containsPart(secondPart));
		this.section.addPart(secondPart);

		Assert.assertTrue(this.section.containsPart(secondPart));
	}
}
