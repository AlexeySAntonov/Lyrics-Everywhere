package alexejantonov.com.musixmatch_lyrics_api_app.ui.artists_and_tracks;

import android.support.v7.widget.RecyclerView;
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

	public DataAdapter(List<BaseData> data,
	                   OnTrackClickListener listener,
	                   OnTwitterClickListener onTwitterClickListener,
	                   RequestManager imageRequestManager) {

		this.data = data;
		this.listener = listener;
		this.onTwitterClickListener = onTwitterClickListener;
		this.imageRequestManager = imageRequestManager;
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

	private class ArtistViewHolder extends RecyclerView.ViewHolder {

		private TextView artistName;
		private ImageView twitterImage;

		public ArtistViewHolder(View itemView) {
			super(itemView);

			artistName = itemView.findViewById(R.id.artistName);
			twitterImage = itemView.findViewById(R.id.twitterIcon);
			twitterImage.setOnClickListener(view -> onTwitterClickListener.onClick(((Artist) data.get(getAdapterPosition())).getTwitterUrl()));
		}

		private void bindTo(Artist artist) {
			artistName.setText(artist.getArtistName());
		}
	}

	private class TrackViewHolder extends RecyclerView.ViewHolder {

		private TextView trackName;
		private TextView trackAlbum;
		private ImageView albumCover;

		public TrackViewHolder(View itemView, OnTrackClickListener listener) {
			super(itemView);

			trackName = itemView.findViewById(R.id.trackName);
			trackAlbum = itemView.findViewById(R.id.trackAlbum);
			albumCover = itemView.findViewById(R.id.albumCover);

			itemView.setOnClickListener(view -> listener.onClick((Track) data.get(getAdapterPosition())));
		}

		private void bindTo(Track track) {
			trackName.setText(track.getTrackName());
			trackAlbum.setText(track.getAlbumName());
			imageRequestManager.load(track.getAlbumCover()).into(albumCover);
			Log.d("album_cover", track.getAlbumCover());
		}
	}

	interface OnTrackClickListener {
		void onClick(Track track);
	}

	interface OnTwitterClickListener {
		void onClick(String twitterUrl);
	}
}
