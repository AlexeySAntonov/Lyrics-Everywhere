package alexejantonov.com.musixmatch_lyrics_api_app.ui.Base;

import android.support.v7.util.DiffUtil;

import java.util.List;

public class DataDiffCallback extends DiffUtil.Callback {

	private final List<BaseData> oldList;
	private final List<BaseData> newList;

	public DataDiffCallback(List<BaseData> oldList, List<BaseData> newList) {
		this.oldList = oldList;
		this.newList = newList;
	}

	@Override
	public int getOldListSize() {
		return oldList.size();
	}

	@Override
	public int getNewListSize() {
		return newList.size();
	}

	@Override
	public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
		return oldList.get(oldItemPosition).getArtistId() == newList.get(newItemPosition).getArtistId();
	}

	@Override
	public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
		return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
	}
}
