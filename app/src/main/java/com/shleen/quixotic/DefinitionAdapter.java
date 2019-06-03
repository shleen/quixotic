package com.shleen.quixotic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DefinitionAdapter extends ArrayAdapter<Definition> {

    private Context mContext;
    private int resourceLayout;
    private List<Definition> definitions;

    public DefinitionAdapter(Context context, int resource, ArrayList<Definition> definitions) {
        super(context, resource, definitions);

        this.mContext = context;
        this.resourceLayout = resource;
        this.definitions = definitions;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(resourceLayout, null);
        }

        Definition currentDefinition = definitions.get(position);

        TextView txt_word_type = (TextView) v.findViewById(R.id.word_type);
        txt_word_type.setText(currentDefinition.getWord_type());

        TextView txt_definition = (TextView) v.findViewById(R.id.definition);
        txt_definition.setText(currentDefinition.getDefinition());

        return v;

    }
}
