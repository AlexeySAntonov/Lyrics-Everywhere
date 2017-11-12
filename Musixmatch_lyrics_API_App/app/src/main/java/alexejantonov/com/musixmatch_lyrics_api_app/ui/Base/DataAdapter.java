package alexejantonov.com.musixmatch_lyrics_api_app.ui.Base;

import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;

import java.util.List;

import alexejantonov.com.musixmatch_lyrics_api_app.R;
import alexejantonov.com.musixmatch_lyrics_api_app.api.entities.artist.Artist;
import alexejantonov.com.musixmatch_lyrics_api_app.api.entities.track.Track;

public class DataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private List<BaseData> data;
	private OnTrackClickListener listener;
	private OnTwitterClickListener onTwitterClickListener;
	private RequestManager imageRequestManager;
	private String query;

	public DataAdapter(List<BaseData> data,
	                   OnTrackClickListener listener,
	                   OnTwitterClickListener onTwitterClickListener,
	                   RequestManager imageRequestManager, String query) {

		this.data = data;
		this.listener = listener;
		this.onTwitterClickListener = onTwitterClickListener;
		this.imageRequestManager = imageRequestManager;
		this.query = query;
	}

	@Override
	public int getItemViewType(int position) {
		if (data.get(position) instanceof Artist) {
			return 0;
		} else {
			return 1;
		}
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view;
		RecyclerView.ViewHolder viewHolder;

		switch (viewType) {
			case 0:
				view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artist, parent, false);
				viewHolder = new ArtistViewHolder(view);
				break;
			case 1:
				view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_track, parent, false);
				viewHolder = new TrackViewHolder(view, listener);
				break;
			default:
				viewHolder = null;
				break;
		}

		return viewHolder;
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		switch (this.getItemViewType(position)) {
			case 0:
				ArtistViewHolder artistHolder = (ArtistViewHolder) holder;
				artistHolder.bindTo((Artist) data.get(position));
				break;
			case 1:
				TrackViewHolder trackHolder = (TrackViewHolder) holder;
				trackHolder.bindTo((Track) data.get(position));
				break;
		}
	}

	@Override
	public int getItemCount() {
		return data.size();
	}

	public void updateData(List<BaseData> data) {
		DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DataDiffCallback(this.data, data));

		this.data.clear();
		this.data.addAll(data);
		diffResult.dispatchUpdatesTo(this);
	}

	public void updateQueryData(List<BaseData> data, String query) {
		this.data = data;
		this.query = query;
		notifyDataSetChanged();
	}

	private class ArtistViewHolder extends RecyclerView.ViewHolder {

		private TextView artistName;
		private ImageView twitterImage;

		ArtistViewHolder(View itemView) {
			super(itemView);

			artistName = itemView.findViewById(R.id.artistName);
			twitterImage = itemView.findViewById(R.id.twitterIcon);
			twitterImage.setOnClickListener(view -> onTwitterClickListener.onClick(((Artist) data.get(getAdapterPosition())).getTwitterUrl()));
		}

		private void bindTo(Artist artist) {
			if (query != null) {
				int startIndex = indexOfSearchQuery(artist.getArtistName());
				if (startIndex == -1) {
					artistName.setText(artist.getArtistName());
				} else {
					SpannableString highLightedName = new SpannableString(artist.getArtistName());
					highLightedName.setSpan(
							new TextAppearanceSpan(itemView.getContext(), R.style.searchTextHighlight),
							startIndex,
							startIndex + query.length(),
							0);
					artistName.setText(highLightedName);
				}
			} else {
				artistName.setText(artist.getArtistName());
			}
		}

		private int indexOfSearchQuery(String artistName) {
			if (!TextUtils.isEmpty(query)) {
				return artistName.toLowerCase().indexOf(query.toLowerCase());
			}
			return -1;
		}
	}

	private class TrackViewHolder extends RecyclerView.ViewHolder {

		private TextView trackName;
		private TextView trackAlbum;
		private ImageView albumCover;

		TrackViewHolder(View itemView, OnTrackClickListener listener) {
			super(itemView);

			trackName = itemView.findViewById(R.id.trackName);
			trackAlbum = itemView.findViewById(R.id.trackAlbum);
			albumCover = itemView.findViewById(R.id.albumCover);

			itemView.setOnClickListener(view -> listener.onClick((Track) data.get(getAdapterPosition())));
		}

		private void bindTo(Track track) {
			if (query != null) {
				int startIndex = indexOfSearchQuery(track.getTrackName());
				if (startIndex == -1) {
					trackName.setText(track.getTrackName());
				} else {
					SpannableString highLightedName = new SpannableString(track.getTrackName());
					highLightedName.setSpan(
							new TextAppearanceSpan(itemView.getContext(), R.style.searchTextHighlight),
							startIndex,
							startIndex + query.length(),
							0);
					trackName.setText(highLightedName);
				}
			} else {
				trackName.setText(track.getTrackName());
			}
			trackAlbum.setText(track.getAlbumName());
			imageRequestManager.load(track.getAlbumCover()).into(albumCover);
			Log.d("album_cover", track.getAlbumCover());
		}

		private int indexOfSearchQuery(String artistName) {
			if (!TextUtils.isEmpty(query)) {
				return artistName.toLowerCase().indexOf(query.toLowerCase());
			}
			return -1;
		}
	}

	public interface OnTrackClickListener {
		void onClick(Track track);
	}

	public interface OnTwitterClickListener {
		void onClick(String twitterUrl);
	}
}
