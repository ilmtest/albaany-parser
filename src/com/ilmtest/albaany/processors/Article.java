package com.ilmtest.albaany.processors;

public class Article
{
	public int tape;
	public int inTapeSegment;
	public int pageNumber;
	public String body;
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((body == null) ? 0 : body.hashCode());
		result = prime * result + inTapeSegment;
		result = prime * result + pageNumber;
		result = prime * result + tape;
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (!(obj instanceof Article))
		{
			return false;
		}
		Article other = (Article) obj;
		if (body == null)
		{
			if (other.body != null)
			{
				return false;
			}
		} else if (!body.equals(other.body))
		{
			return false;
		}
		if (inTapeSegment != other.inTapeSegment)
		{
			return false;
		}
		if (pageNumber != other.pageNumber)
		{
			return false;
		}
		if (tape != other.tape)
		{
			return false;
		}
		return true;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Entry [tape=" + tape + ", inTapeSegment=" + inTapeSegment + ", pageNumber=" + pageNumber;
	}
}